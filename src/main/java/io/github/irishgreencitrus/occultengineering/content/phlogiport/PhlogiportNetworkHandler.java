package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.createmod.catnip.levelWrappers.WorldHelper;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

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
            return;
        }
        updateNetworkOf(world, phlogiport);
    }

    public @Nullable IPhlogiportNetworkable findMatchingPhlogiport(LevelAccessor world, IPhlogiportNetworkable sender, String destinationAddress) {
        if (Objects.equals(sender.getAddress(), destinationAddress)) return null;

        var worldNetwork = getNetworkFor(world);
        // TODO: implement wildcards...
        if (!worldNetwork.containsKey(destinationAddress)) return null;


        var possiblePhlogiports = worldNetwork.get(destinationAddress);
        if (possiblePhlogiports.isEmpty()) return null;


        var maybePort = possiblePhlogiports
                .stream()
                .skip(randomInstance.nextInt(possiblePhlogiports.size()))
                .findFirst();

        if (maybePort.isEmpty()) return null;

        var port = maybePort.get();

        // Don't send packages to ourselves.
        if (sender == port) return null;

        // More than 128 blocks away? Don't send a package.
        if (sender.getLocation().distSqr(port.getLocation()) > 16384) return null;


        // I have no idea why this isn't exposed in LevelAccessor, but it is in Level.
        // Basically Level.isLoaded(Position)
        var isLoaded = world.getChunkSource()
                .hasChunk(
                        SectionPos.blockToSectionCoord(port.getLocation().getX()),
                        SectionPos.blockToSectionCoord(port.getLocation().getZ()));

        if (!isLoaded) return null;
        return port;
    }

    public void updateNetworkOf(LevelAccessor world, IPhlogiportNetworkable phlogiport) {
        // TODO, although we may not need it.
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
