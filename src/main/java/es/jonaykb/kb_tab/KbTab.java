package es.jonaykb.kb_tab;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Mod(KbTab.MOD_ID)
@Mod.EventBusSubscriber(modid = KbTab.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class KbTab {

    public static final String MOD_ID = "kb_tab";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    /* ================= CONFIG ================= */
    private static final int TAB_UPDATE_TICKS = 20 * 5; // 5 segundos

    /* ================= DATA ================= */
    private static final Map<UUID, Long> SESSION_START = new HashMap<>();
    private static final Map<UUID, Long> LAST_ACTIVITY = new HashMap<>();

    /* ================= HEADER ANIMATION ================= */
    private static final String[] HEADER_FRAMES = {
            "§6§lMCKB§e§lSERVER",
            "§e§lMCKB§6§lSERVER",
            "§f§lMCKB§e§lSERVER"
    };
    private static int headerIndex = 0;

    public KbTab() {
        LOGGER.info("KB TAB + AFK initialized");
    }

    /* ================= PLAYER JOIN ================= */
    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        long now = System.currentTimeMillis();

        SESSION_START.put(player.getUUID(), now);
        LAST_ACTIVITY.put(player.getUUID(), now);

        updateTab(player);
    }

    /* ================= PLAYER LEAVE ================= */
    @SubscribeEvent
    public static void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID id = event.getEntity().getUUID();
        SESSION_START.remove(id);
        LAST_ACTIVITY.remove(id);
    }

    /* ================= UPDATE TAB ================= */
    private static void updateTab(ServerPlayer player) {
        UUID id = player.getUUID();

        long now = System.currentTimeMillis();
        long sessionMs = now - SESSION_START.getOrDefault(id, now);

        long sessionMinutes = sessionMs / 60000;
        long sessionHours = sessionMinutes / 60;

        long playTicks = player.getStats()
                .getValue(Stats.CUSTOM.get(Stats.PLAY_TIME));
        long totalHours = (playTicks / 20) / 3600;

        int deaths = player.getStats()
                .getValue(Stats.CUSTOM.get(Stats.DEATHS));

        int mobKills = player.getStats()
                .getValue(Stats.CUSTOM.get(Stats.MOB_KILLS));

        Component header = Component.literal(
                HEADER_FRAMES[headerIndex % HEADER_FRAMES.length]);
        headerIndex++;

        Component footer = Component.literal(
                "§7┌──────────────────┐\n" +
                        "§7 §eSesión: §f" +
                        sessionHours + "h " + (sessionMinutes % 60) + "m" + "\n" +
                        "§7 §cMuertes: §f" + deaths + "\n" +
                        "§7 §aKills: §f" + mobKills + "\n" +
                        "§7 §6Total: §f" + totalHours + "h\n" +
                        "§7└──────────────────┘");

        player.connection.send(new ClientboundTabListPacket(header, footer));
    }

    /* ================= SERVER TICK ================= */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null)
            return;

        // Actualizar TAB cada 5s
        if (server.getTickCount() % TAB_UPDATE_TICKS != 0)
            return;

        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            updateTab(p);
        }
    }
}
