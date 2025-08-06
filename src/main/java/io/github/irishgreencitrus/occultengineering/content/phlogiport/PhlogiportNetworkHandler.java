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

        Set<IPhlogiportNetworkable> possiblePhlogiports;

        if (worldNetwork.containsKey(destinationAddress))
            possiblePhlogiports = worldNetwork.get(destinationAddress);
        else
            possiblePhlogiports = worldNetwork
                    .values()
                    .stream()
                    .flatMap(Set::stream)
                    .filter(p ->
                            PackageItem.matchAddress(destinationAddress, p.getAddress()))
                    .collect(Collectors.toSet());

        if (possiblePhlogiports.isEmpty()) return null;

        // Don't send packages to ourselves.
        possiblePhlogiports.remove(sender);

        // Don't send a package if we're too far away.
        possiblePhlogiports.removeIf(receiver -> isInRange(sender, receiver));

        // This may never actually matter, because we *should* remove
        // every Phlogiport from the network when they are unloaded.

        // Let's keep this check here just in case something completely fails.

        // Don't send a package if the chunk is unloaded.
        possiblePhlogiports.removeIf(receiver ->
                world.getChunkSource()
                        .hasChunk(
                                SectionPos.blockToSectionCoord(receiver.getLocation().getX()),
                                SectionPos.blockToSectionCoord(receiver.getLocation().getZ()))
        );

        if (possiblePhlogiports.isEmpty()) return null;

        return possiblePhlogiports
                .stream()
                .skip(randomInstance.nextInt(possiblePhlogiports.size()))
                .findFirst()
                .orElse(null);
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
