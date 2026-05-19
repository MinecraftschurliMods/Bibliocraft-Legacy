package at.minecraftschurli.mods.bibliocraft.client.model;

import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.cuboid.CuboidModel;
import net.minecraft.client.resources.model.cuboid.MissingCuboidModel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;

public final class ConditionalModelLoader implements UnbakedModelLoader<UnbakedModel> {
    public static final Identifier ID = BCUtil.bcLoc("conditional");

    @Override
    public UnbakedModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
        JsonArray conditionArray = GsonHelper.getAsJsonArray(json, ConditionalOps.DEFAULT_CONDITIONS_KEY);
        if (ICondition.LIST_CODEC.decode(JsonOps.INSTANCE, conditionArray)
            .getOrThrow(e -> new JsonParseException("Failed to parse conditions: " + e))
            .getFirst()
            .stream()
            .allMatch(e -> e.test(ICondition.IContext.EMPTY))) {
            json.remove("loader");
            return context.deserialize(json, CuboidModel.class);
        }
        return MissingCuboidModel.missingModel();
    }
}
