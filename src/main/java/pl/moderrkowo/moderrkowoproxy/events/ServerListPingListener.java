package pl.moderrkowo.moderrkowoproxy.events;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import pl.moderrkowo.moderrkowoproxy.sockets.ServerStatus;

import java.io.IOException;
import java.util.UUID;

public class ServerListPingListener {

    final ServerPing.SamplePlayer header = new ServerPing.SamplePlayer("§6§lModerrkowo", UUID.randomUUID());
    final ServerPing.SamplePlayer www = new ServerPing.SamplePlayer("§6Strona §f> moderrkowo.pl", UUID.randomUUID());
    final ServerPing.SamplePlayer store = new ServerPing.SamplePlayer("§6Sklep §f> sklep.moderrkowo.pl", UUID.randomUUID());


    @Subscribe
    public EventTask onProxyPingEvent(ProxyPingEvent e) {
        return EventTask.async(() -> this.ping(e));
    }

    private void ping(@NotNull ProxyPingEvent e) {
        final ServerPing.Builder ping = e.getPing().asBuilder();
        ping.clearSamplePlayers();
        ping.samplePlayers(header, www, store);
        ServerStatus status = new ServerStatus("94.23.253.210", 30067);
        try {
            status.update();
        } catch (IOException ex) {
            ping.description(Component.text("Serwer offline").color(NamedTextColor.RED));
            e.setPing(ping.build());
            return;
        }
        ping.description(LegacyComponentSerializer.legacySection().deserialize(status.getStatus()));
        e.setPing(ping.build());
    }

}
