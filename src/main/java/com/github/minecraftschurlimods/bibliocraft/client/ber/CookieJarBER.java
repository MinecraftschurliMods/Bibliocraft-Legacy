package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class CookieJarBER implements BlockEntityRenderer<CookieJarBlockEntity, CookieJarBER.State> {
    private final ItemModelResolver itemModelResolver;

    public CookieJarBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    private void cookiePosition(int index, PoseStack stack) {
        switch (index) {
            case 0:
                stack.translate(-0.1, -0.425, 0.1);
                break;
            case 1:
                stack.translate(-0.0875, -0.3875, -0.0875);
                stack.mulPose(Axis.XP.rotationDegrees(-15));
                break;
            case 2:
                stack.translate(0.1, -0.3325, 0);
                stack.mulPose(Axis.ZP.rotationDegrees(-20));
                break;
            case 3:
                stack.translate(0.1, -0.295, -0.125);
                stack.mulPose(Axis.XP.rotationDegrees(-15));
                stack.mulPose(Axis.ZP.rotationDegrees(-15));
                break;
            case 4:
                stack.translate(-0.1, -0.245, 0.15);
                stack.mulPose(Axis.XP.rotationDegrees(35));
                stack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case 5:
                stack.translate(-0.1, -0.1625, -0.125);
                stack.mulPose(Axis.XP.rotationDegrees(-30));
                stack.mulPose(Axis.ZP.rotationDegrees(5));
                break;
            case 6:
                stack.translate(-0.1, -0.1325, 0.1575);
                stack.mulPose(Axis.XP.rotationDegrees(45));
                stack.mulPose(Axis.ZP.rotationDegrees(10));
                stack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case 7:
                stack.translate(0.2, -0.125, 0.1);
                stack.mulPose(Axis.XP.rotationDegrees(30));
                stack.mulPose(Axis.ZP.rotationDegrees(-60));
                stack.mulPose(Axis.YP.rotationDegrees(-105));
                break;
        }
        stack.mulPose(Axis.XP.rotationDegrees(90));
        stack.scale(0.6f, 0.6f, 0.6f);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(CookieJarBlockEntity blockEntity, State state, float partialTicks, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, p_445788_, p_446944_);
        LegacyRandomSource random = new LegacyRandomSource(blockEntity.getBlockPos().asLong());
        int i = HashCommon.long2int(blockEntity.getBlockPos().asLong());
        List<ItemStack> items = IntStream.rangeClosed(0, blockEntity.getContainerSize() - 1)
                .mapToObj(blockEntity::getItem)
                .filter(e -> !e.isEmpty())
                .toList();
        List<ItemStack> cookies = BuiltInRegistries.ITEM.get(BCTags.Items.COOKIE_JAR_COOKIES)
                .orElseThrow()
                .stream()
                .sorted(Comparator.comparing(a -> BuiltInRegistries.ITEM.getKey(a.value())))
                .map(ItemStack::new)
                .toList();
        state.items = new ItemStackRenderState[items.size()];
        for (int j = 0; j < items.size(); j++) {
            ItemStack item = items.get(j);
            ItemStack cookie = cookies.get(random.nextInt(cookies.size()));
            ItemStack stack = item.is(BCTags.Items.COOKIE_JAR_COOKIES) ? item : cookie;
            ItemStackRenderState itemstackrenderstate = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(itemstackrenderstate, stack, ItemDisplayContext.FIXED, blockEntity.level(), blockEntity, i + j);
            state.items[j] = itemstackrenderstate;
        }
    }

    @Override
    public void submit(State state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState p_451022_) {
        stack.translate(0.5, 0.5, 0.5);
        for (int i = 0; i < state.items.length; i++) {
            if (state.items[i] == null) {
                continue;
            }
            stack.pushPose();
            cookiePosition(i, stack);
            state.items[i].submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            stack.popPose();
        }
    }
    
    public static class State extends BlockEntityRenderState {
        public ItemStackRenderState[] items;
    }
}
