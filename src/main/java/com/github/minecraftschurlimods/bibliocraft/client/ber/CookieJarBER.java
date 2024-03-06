package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.LegacyRandomSource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class CookieJarBER implements BlockEntityRenderer<CookieJarBlockEntity> {
    private final RandomSource random = new LegacyRandomSource(0);

    @SuppressWarnings("unused")
    public CookieJarBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CookieJarBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        List<ItemStack> items = IntStream.rangeClosed(0, blockEntity.getContainerSize() - 1)
                .mapToObj(blockEntity::getItem)
                .filter(e -> !e.isEmpty())
                .toList();
        List<ItemStack> cookies = BuiltInRegistries.ITEM.getTag(BCTags.Items.COOKIE_JAR_COOKIES)
                .orElseThrow()
                .stream()
                .sorted(Comparator.comparing(a -> BuiltInRegistries.ITEM.getKey(a.value())))
                .map(ItemStack::new)
                .toList();
        random.setSeed(blockEntity.getBlockPos().asLong());
        stack.translate(0.5, 0.5, 0.5);
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            ItemStack cookie = cookies.get(random.nextInt(cookies.size()));
            stack.pushPose();
            cookiePosition(i, stack);
            ClientUtil.renderFixedItem(item.is(BCTags.Items.COOKIE_JAR_COOKIES) ? item : cookie, stack, buffer, light, overlay);
            stack.popPose();
        }
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
}
