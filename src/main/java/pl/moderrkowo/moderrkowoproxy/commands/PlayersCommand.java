package pl.moderrkowo.moderrkowoproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
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

public final class PlayersCommand implements SimpleCommand {

    private ModerrkowoProxy proxy;

    public PlayersCommand(ModerrkowoProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final @NotNull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        for (RegisteredServer server : proxy.server.getAllServers()) {
            source.sendMessage(Component.text(server.getServerInfo().getName()).color(NamedTextColor.YELLOW).appendSpace().append(Component.text(server.getPlayersConnected().size()).color(NamedTextColor.GREEN)));
            ComponentBuilder<TextComponent, TextComponent.Builder> builder = Component.text("").toBuilder();
            for (int i = 0; i < server.getPlayersConnected().size(); i += 1) {
                Player player = (Player) server.getPlayersConnected().toArray()[i];
                if (i > 0)
                    builder.append(Component.text(", "));
                builder.append(Component.text(player.getUsername()));
            }
            source.sendMessage(builder.build());
        }
    }

    @Override
    public boolean hasPermission(final @NotNull Invocation invocation) {
        return invocation.source().hasPermission("moderrkowo.players");
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
