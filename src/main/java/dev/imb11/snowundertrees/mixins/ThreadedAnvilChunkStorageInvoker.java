package dev.imb11.snowundertrees.mixins;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkLoadingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/*? if <1.21 {*/
/*@Mixin(net.minecraft.server.world.ThreadedAnvilChunkStorage.class)
*//*?} else {*/
@Mixin(ServerChunkLoadingManager.class)
/*?}*/
public interface ThreadedAnvilChunkStorageInvoker {
    @Invoker("entryIterator")
    Iterable<ChunkHolder> invokeEntryIterator();
}
