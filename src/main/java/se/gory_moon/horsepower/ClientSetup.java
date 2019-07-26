package se.gory_moon.horsepower;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import se.gory_moon.horsepower.blocks.ModBlocks;
import se.gory_moon.horsepower.client.renderer.TileEntityFillerRender;
import se.gory_moon.horsepower.client.renderer.TileEntityHandMillstoneRender;
import se.gory_moon.horsepower.client.renderer.TileEntityMillstoneRender;
import se.gory_moon.horsepower.client.renderer.TileEntityPressRender;
import se.gory_moon.horsepower.lib.Reference;
import se.gory_moon.horsepower.tileentity.FillerTileEntity;
import se.gory_moon.horsepower.tileentity.TileEntityHandMillstone;
import se.gory_moon.horsepower.tileentity.TileEntityMillstone;
import se.gory_moon.horsepower.tileentity.TileEntityPress;
import se.gory_moon.horsepower.util.color.ColorGetter;

@Mod.EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMillstone.class, new TileEntityMillstoneRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHandMillstone.class, new TileEntityHandMillstoneRender());
        ClientRegistry.bindTileEntitySpecialRenderer(FillerTileEntity.class, new TileEntityFillerRender());
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChopper.class, new TileEntityChopperRender());
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityManualChopper.class, new TileEntityChoppingBlockRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPress.class, new TileEntityPressRender());

        //TODO make server command
        //ClientCommandHandler.instance.registerCommand(new HorsePowerCommand());

        Minecraft.getInstance().getBlockColors().register((state, worldIn, pos, tintIndex) -> {
            if (worldIn != null && pos != null) {
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if (tileEntity instanceof TileEntityMillstone) {
                    TileEntityMillstone te = (TileEntityMillstone) tileEntity;
                    ItemStack outputStack = te.getStackInSlot(1);
                    ItemStack secondaryStack = te.getStackInSlot(2);
                    if (outputStack.getCount() < secondaryStack.getCount())
                        outputStack = secondaryStack;
                    if (!ItemStack.areItemsEqual(te.renderStack, outputStack)) {
                        te.renderStack = outputStack;
                        if (!outputStack.isEmpty())
                            te.millColor = ColorGetter.getColors(outputStack, 1).get(0);
                        else
                            te.millColor = -1;
                        te.renderStack = outputStack;
                    }

                    if (te.millColor != -1)
                        return te.millColor;
                }
            }
            return -1;
        }, ModBlocks.BLOCK_MILLSTONE.orElseThrow(RuntimeException::new));
    }
}
