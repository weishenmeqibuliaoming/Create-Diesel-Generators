package com.jesz.createdieselgenerators.blocks.renderer;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.bearing.IBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.kineticRotationTransform;

public class NoShaftBearingRenderer<T extends KineticBlockEntity & IBearingBlockEntity> extends SafeBlockEntityRenderer<T> {

    public NoShaftBearingRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }
    @Override
    protected void renderSafe(T be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        if (Backend.canUseInstancing(be.getLevel())) return;

        final Direction facing = be.getBlockState()
                .getValue(BlockStateProperties.FACING);
        PartialModel top =
                be.isWoodenTop() ? AllPartialModels.BEARING_TOP_WOODEN : AllPartialModels.BEARING_TOP;
        SuperByteBuffer superBuffer = CachedBufferer.partial(top, be.getBlockState());

        float interpolatedAngle = be.getInterpolatedAngle(partialTicks - 1);
        kineticRotationTransform(superBuffer, be, facing.getAxis(), (float) (interpolatedAngle / 180 * Math.PI), light);
        if (facing.getAxis()
                .isHorizontal())
            superBuffer.rotateCentered(Direction.UP,
                    AngleHelper.rad(AngleHelper.horizontalAngle(facing.getOpposite())));
        superBuffer.rotateCentered(Direction.EAST, AngleHelper.rad(-90 - AngleHelper.verticalAngle(facing)));
        superBuffer.light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }
}
