package at.minecraftschurli.mods.bibliocraft.init;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface BCFluids {
    DeferredHolder<FluidType, ?> EXPERIENCE_TYPE = BCRegistries.FLUID_TYPES.register("experience", () -> new FluidType(FluidType.Properties.create()));
    DeferredHolder<Fluid, ?> EXPERIENCE = BCRegistries.FLUIDS.register("experience", () -> new BaseFlowingFluid.Source(BCFluids.EXPERIENCE_PROPERTIES));
    BaseFlowingFluid.Properties EXPERIENCE_PROPERTIES = new BaseFlowingFluid.Properties(EXPERIENCE_TYPE, EXPERIENCE, EXPERIENCE);

    static void init() {}
}
