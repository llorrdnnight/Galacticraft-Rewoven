package io.github.teamgalacticraft.galacticraft.world.gen.chunk;

import io.github.teamgalacticraft.galacticraft.Constants;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

import java.util.function.Supplier;

public class GCChunkGeneratorTypes {

    public static final ChunkGeneratorType MOON = register("moon", MoonChunkGeneratorConfig::new, true);


    private static <C extends ChunkGeneratorConfig> ChunkGeneratorType register(String string_1, Supplier<C> supplier_1, boolean boolean_1) {
        return Registry.register(Registry.CHUNK_GENERATOR_TYPE, new Identifier(Constants.MOD_ID, string_1), new ChunkGeneratorType<>(null, boolean_1, supplier_1));
    }
}