package at.minecraftschurli.mods.bibliocraft.client.model;

import at.minecraftschurli.mods.bibliocraft.util.CodecUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;

public class ConditionalModelLoaderBuilder extends CustomLoaderBuilder {
    private final List<ICondition> conditions = new ArrayList<>();

    public ConditionalModelLoaderBuilder() {
        super(ConditionalModelLoader.ID, false);
    }

    @Override
    protected CustomLoaderBuilder copyInternal() {
        ConditionalModelLoaderBuilder builder = new ConditionalModelLoaderBuilder();
        builder.conditions.addAll(conditions);
        return builder;
    }

    public ConditionalModelLoaderBuilder addConditions(ICondition... conditions) {
        this.conditions.addAll(List.of(conditions));
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        JsonArray array = new JsonArray();
        for (ICondition condition : conditions) {
            array.add(CodecUtil.encodeJson(ICondition.CODEC, condition));
        }
        json.add(ConditionalOps.DEFAULT_CONDITIONS_KEY, array);
        return super.toJson(json);
    }
}
