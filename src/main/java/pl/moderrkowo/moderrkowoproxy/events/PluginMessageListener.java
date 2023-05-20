package pl.moderrkowo.moderrkowoproxy.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import pl.moderrkowo.moderrkowoproxy.ModerrkowoProxy;

public class PluginMessageListener {

    private final ModerrkowoProxy proxy;
    private final ProxyServer server;

    public PluginMessageListener(ModerrkowoProxy proxy) {
        this.proxy = proxy;
        this.server = this.proxy.server;
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
//        if(event.getIdentifier().)
    }

}
