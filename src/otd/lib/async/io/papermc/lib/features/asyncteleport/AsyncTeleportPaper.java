package otd.lib.async.io.papermc.lib.features.asyncteleport;

import otd.lib.async.io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.concurrent.CompletableFuture;

public class AsyncTeleportPaper implements AsyncTeleport {

    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, TeleportCause cause) {
        int x = location.getBlockX() >> 4;
        int z = location.getBlockZ() >> 4;
        return PaperLib.getChunkAtAsyncUrgently(location.getWorld(), x, z, true).thenApply(chunk -> entity.teleport(location, cause));
    }

}
