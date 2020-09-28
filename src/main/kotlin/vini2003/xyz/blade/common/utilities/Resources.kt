package vini2003.xyz.blade.common.utilities

import net.minecraftforge.event.AddReloadListenerEvent
import java.util.concurrent.CompletableFuture

class Resources {
	companion object {
		fun reload(event: AddReloadListenerEvent) {
			event.addListener { stage, manager, _, _, prepareExecutor, applyExecutor ->
				CompletableFuture.runAsync({
					Styles.clear()
				}, prepareExecutor).thenCompose(stage::wait).thenRunAsync({
					Styles.load(manager)
				}, applyExecutor)
			}
		}
	}
}
