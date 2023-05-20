package pl.moderrkowo.moderrkowoproxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import pl.moderrkowo.moderrkowoproxy.services.CommandService;
import pl.moderrkowo.moderrkowoproxy.services.EventService;

@Plugin(
        id = "moderrkowo-proxy",
        name = "Moderrkowo Proxy",
        description = "Proxy",
        version = "1.0-SNAPSHOT",
        authors = {"MODERR"}
)
public class ModerrkowoProxy {

    public final ProxyServer server;
    public final Logger logger;

    private final EventService eventService;
    private final CommandService commandService;

    @Inject
    public ModerrkowoProxy(ProxyServer server, Logger logger) {
        this.logger = logger;
        this.server = server;
        this.eventService = new EventService();
        this.commandService = new CommandService();
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        this.eventService.start(this, server);
        this.commandService.start(this, server);
        logger.info("Successfully loaded ModerrkowoProxy.");
    }
}
