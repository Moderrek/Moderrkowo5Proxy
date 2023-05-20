package pl.moderrkowo.moderrkowoproxy.events;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.moderrkowo.moderrkowoproxy.ModerrkowoProxy;

import java.text.MessageFormat;

public class ServerConnectionListener {

    private final ModerrkowoProxy proxy;
    private final ProxyServer server;

    @Contract(pure = true)
    public ServerConnectionListener(@NotNull ModerrkowoProxy proxy) {
        this.proxy = proxy;
        this.server = proxy.server;
    }

    @Subscribe
    public EventTask onProxyPingEvent(ServerConnectedEvent e) {
        return EventTask.async(() -> this.join(e));
    }

    @Subscribe
    public EventTask onProxyPlayerDisconnect(DisconnectEvent e) {
        return EventTask.async(() -> this.quit(e));
    }

    private void sendMessage(Component component) {
        for (Player player : server.getAllPlayers()) {
            if (!player.hasPermission("moderrkowo.players")) {
                continue;
            }
            player.sendMessage(component);
        }
    }

    private void join(@NotNull ServerConnectedEvent e) {
        final boolean isChangedServer = e.getPreviousServer().isPresent();
        final String username = e.getPlayer().getUsername();
        final String toServerName = e.getServer().getServerInfo().getName();
        if (isChangedServer) {
            final String fromServerName = e.getPreviousServer().get().getServerInfo().getName();
            final Component changedServer = Component
                    .text("[PROXY] ")
                    .color(NamedTextColor.RED).append(
                            Component.text(MessageFormat.format("{0} {1} -> {2}", username, fromServerName, toServerName))
                                    .color(NamedTextColor.YELLOW)
                    );
            sendMessage(changedServer);
            return;
        }
        final Component joinedServer = Component
                .text("[PROXY] ")
                .color(NamedTextColor.RED).append(
                        Component.text(MessageFormat.format("{0} joined -> {1}", username, toServerName))
                                .color(NamedTextColor.YELLOW)
                );
        sendMessage(joinedServer);
    }

    private void quit(@NotNull DisconnectEvent e) {
        final String username = e.getPlayer().getUsername();
        final Component leavedServer = Component
                .text("[PROXY] ")
                .color(NamedTextColor.RED).append(
                        Component.text(MessageFormat.format("{0} left <-", username))
                                .color(NamedTextColor.YELLOW)
                );
        sendMessage(leavedServer);
    }


}
