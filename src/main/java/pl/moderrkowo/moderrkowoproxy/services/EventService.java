package pl.moderrkowo.moderrkowoproxy.services;

import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import pl.moderrkowo.moderrkowoproxy.ModerrkowoProxy;
import pl.moderrkowo.moderrkowoproxy.events.PluginMessageListener;
import pl.moderrkowo.moderrkowoproxy.events.ServerConnectionListener;
import pl.moderrkowo.moderrkowoproxy.events.ServerListPingListener;

public class EventService implements ModerrkowoService {
    @Override
    public void start(ModerrkowoProxy proxy, @NotNull ProxyServer server) {
        server.getEventManager().register(this, new ServerListPingListener());
        server.getEventManager().register(this, new ServerConnectionListener(proxy));
        server.getEventManager().register(this, new PluginMessageListener(proxy));
        proxy.logger.info("Loaded EventService");
    }
}
