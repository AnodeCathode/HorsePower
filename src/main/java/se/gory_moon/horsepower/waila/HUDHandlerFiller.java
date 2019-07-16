package se.gory_moon.horsepower.waila;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import se.gory_moon.horsepower.tileentity.TileEntityFiller;
import se.gory_moon.horsepower.tileentity.TileEntityHandMillstone;
import se.gory_moon.horsepower.tileentity.TileEntityMillstone;
import se.gory_moon.horsepower.tileentity.TileEntityPress;

import java.util.List;

public final class HUDHandlerFiller implements IComponentProvider, IServerDataProvider<TileEntity> {

    static final HUDHandlerFiller INSTANCE = new HUDHandlerFiller();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        CompoundNBT nbt = accessor.getServerData();
        if (nbt.contains("type", Constants.NBT.TAG_STRING)) {
            String type = nbt.getString("type");
            switch (type) {
                case "horsepower:millstone":
                    HUDHandlerMillstone.INSTANCE.appendBody(tooltip, accessor, config);
                    break;
                case "horsepower:press":
                    HUDHandlerPress.INSTANCE.appendBody(tooltip, accessor, config);
                    break;
            }
        }
    }

    @Override
    public void appendServerData(CompoundNBT data, ServerPlayerEntity player, World world, TileEntity te) {
        if (te instanceof TileEntityFiller) {
            te = ((TileEntityFiller) te).getFilledTileEntity();
            te.write(data);
            if (te instanceof TileEntityMillstone || te instanceof TileEntityHandMillstone)
                data.putString("type", "horsepower:millstone");
//            else if (te instanceof TileEntityChopper || te instanceof TileEntityManualChopper)
//                data.putString("type", "horsepower:chopper");
            else if (te instanceof TileEntityPress)
                data.putString("type", "horsepower:press");
        }
    }
}