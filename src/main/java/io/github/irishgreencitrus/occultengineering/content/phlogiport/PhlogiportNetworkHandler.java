package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import com.simibubi.create.content.logistics.box.PackageItem;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.config.OccultEngineeringConfig;
import net.createmod.catnip.levelWrappers.WorldHelper;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PhlogiportNetworkHandler {
    private static final Map<LevelAccessor, Map<String, Set<IPhlogiportNetworkable>>> phlogiportNetwork = new IdentityHashMap<>();
    private static final Random randomInstance = new Random();

    public void onLoadWorld(LevelAccessor world) {
        phlogiportNetwork.put(world, new HashMap<>());
        OccultEngineering.LOGGER.debug("Added Phlogiport network for {}", WorldHelper.getDimensionID(world));
    }

    public void onUnloadWorld(LevelAccessor world) {
        phlogiportNetwork.remove(world);
        OccultEngineering.LOGGER.debug("Removed Phlogiport network for {}", WorldHelper.getDimensionID(world));
    }

    public void addToNetwork(LevelAccessor world, IPhlogiportNetworkable phlogiport) {
        if (phlogiport.getAddress() == null) return;
        getNetworkOf(world, phlogiport).add(phlogiport);
    }

    public void removeFromNetwork(LevelAccessor world, IPhlogiportNetworkable phlogiport) {
        if (phlogiport.getAddress() == null) return;
        var network = getNetworkOf(world, phlogiport);
        network.remove(phlogiport);
        if (network.isEmpty()) {
            getNetworkFor(world).remove(phlogiport.getAddress());
        }
    }

    public @Nullable IPhlogiportNetworkable findMatchingPhlogiport(LevelAccessor world, IPhlogiportNetworkable sender, String destinationAddress) {
        if (Objects.equals(sender.getAddress(), destinationAddress)) return null;

        var worldNetwork = getNetworkFor(world);

        Set<IPhlogiportNetworkable> possiblePhlogiports = worldNetwork.get(destinationAddress);

        if (possiblePhlogiports == null || possiblePhlogiports.isEmpty()) {
            possiblePhlogiports = worldNetwork
                    .values()
                    .stream()
                    .flatMap(Set::stream)
                    .filter(p -> PackageItem.matchAddress(destinationAddress, p.getAddress()))
                    .collect(Collectors.toSet());
        }


        if (possiblePhlogiports.isEmpty()) return null;

        List<IPhlogiportNetworkable> validReceivers = possiblePhlogiports
                .stream()
                // Don't send packages to ones that aren't receiving
                .filter(IPhlogiportNetworkable::isReceiving)
                // Don't send packages to ourselves.
                .filter(r -> !r.equals(sender))
                // Don't send a package if we're too far away.
                .filter(r -> isInRange(r, sender))
                // Don't send a package if the chunk is unloaded. (This check may never matter, test it).
                .filter(r ->
                        world.getChunkSource()
                                .hasChunk(
                                        SectionPos.blockToSectionCoord(r.getLocation().getX()),
                                        SectionPos.blockToSectionCoord(r.getLocation().getZ()))
                )
                .toList();


        if (validReceivers.isEmpty()) return null;

        return validReceivers
                .get(randomInstance.nextInt(validReceivers.size()));
    }

    public static boolean isInRange(IPhlogiportNetworkable sender, IPhlogiportNetworkable receiver) {
        var distance = OccultEngineeringConfig.server().phlogiportRangeBlocks.get();
        return sender.getLocation().distSqr(receiver.getLocation()) <= (distance * distance);
    }

    public Set<IPhlogiportNetworkable> getNetworkOf(LevelAccessor world, IPhlogiportNetworkable phlogiport) {
        var worldNetwork = getNetworkFor(world);
        var address = phlogiport.getAddress();
        if (!worldNetwork.containsKey(address)) {
            worldNetwork.put(address, new LinkedHashSet<>());
        }
        return worldNetwork.get(address);
    }

    public Map<String, Set<IPhlogiportNetworkable>> getNetworkFor(LevelAccessor world) {
        if (!phlogiportNetwork.containsKey(world)) {
            OccultEngineering.LOGGER.warn("Phlogiport network not initialised in {}", WorldHelper.getDimensionID(world));
            return new HashMap<>();
        }
        return phlogiportNetwork.get(world);
    }
}
