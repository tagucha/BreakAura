package tgcdev.breakaura;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("breakaura")
public class BreakAura {
    public static final BreakAuraEnchantment BREAK_AURA = new BreakAuraEnchantment();

    public BreakAura() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new BreakAuraEnchantment.CallBreakEvent());
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Enchantment> register) {
            register.getRegistry().register(BREAK_AURA);
        }
    }
}
