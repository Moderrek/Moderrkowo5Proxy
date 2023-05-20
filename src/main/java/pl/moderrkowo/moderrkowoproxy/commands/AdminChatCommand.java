package pl.moderrkowo.moderrkowoproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pl.moderrkowo.moderrkowoproxy.ModerrkowoProxy;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdminChatCommand implements SimpleCommand {

    private final ModerrkowoProxy proxy;
    private final ProxyServer server;

    @Contract(pure = true)
    public AdminChatCommand(@NotNull ModerrkowoProxy proxy) {
        this.proxy = proxy;
        this.server = proxy.server;
    }

    private void sendMessage(Component component) {
        for (Player player : server.getAllPlayers()) {
            if (!player.hasPermission("moderrkowo.adminchat")) {
                continue;
            }
            player.sendMessage(component);
        }
    }

    @Override
    public void execute(final @NotNull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!(source instanceof Player)) {
            source.sendMessage(Component.text("Nie jesteś graczem!").color(NamedTextColor.RED));
            return;
        }
        final Player player = (Player) source;
        if (args.length > 0) {
            ComponentBuilder<TextComponent, TextComponent.Builder> component = Component.text("[ADMINCHAT] ").color(NamedTextColor.RED).append(Component.text(player.getUsername()).color(NamedTextColor.WHITE)).toBuilder();
            for (String arg : args) {
                component.appendSpace().append(Component.text(arg).color(NamedTextColor.WHITE));
            }
            sendMessage(component.build());
            return;
        }
        player.sendMessage(Component.text("/adminchat <wiadomość>").color(NamedTextColor.RED));
    }

    @Override
    public boolean hasPermission(final @NotNull Invocation invocation) {
        return invocation.source().hasPermission("moderrkowo.adminchat");
    }

    @Contract(pure = true)
    @Override
    public @Unmodifiable List<String> suggest(final Invocation invocation) {
        return List.of();
    }

    @Override
    public @NotNull CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(List.of());
    }

}
