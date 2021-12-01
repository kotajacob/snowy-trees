package nz.kota.snowytrees;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.feature.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.world.gen.GenerationStep;

import java.util.ArrayList;
import java.util.List;

public class SnowyTrees implements ModInitializer {
	private static final Feature<DefaultFeatureConfig> SNOWY_TREES_FEATURE = new SnowUnderTreeFeature(DefaultFeatureConfig.CODEC);
	public static final ConfiguredFeature<?, ?> SNOWY_TREES_CONFIGURED = SNOWY_TREES_FEATURE.configure(FeatureConfig.DEFAULT);

	private static List<Identifier> biomesToAddTo = new ArrayList<>();

	@Override
	public void onInitialize() {
		Registry.register(Registry.FEATURE, new Identifier("kota", "snowytrees"), SNOWY_TREES_FEATURE);

		RegistryKey<ConfiguredFeature<?, ?>> snowyTrees = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier("kota", "snowytrees"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, snowyTrees.getValue(), SNOWY_TREES_CONFIGURED);

		PlacedFeature SNOWY_TREES_PLACED_FEATURE = SNOWY_TREES_CONFIGURED.withPlacement(BiomePlacementModifier.of());
		RegistryKey<PlacedFeature> snowyTreesPlaced = RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("kota", "snowytrees"));
		Registry.register(BuiltinRegistries.PLACED_FEATURE, snowyTrees.getValue(), SNOWY_TREES_PLACED_FEATURE);

		BiomeModifications.addFeature(b -> shouldAddSnow(b.getBiome()), GenerationStep.Feature.TOP_LAYER_MODIFICATION, snowyTreesPlaced);
	}

	private boolean shouldAddSnow(Biome biome) {
		return biome.getPrecipitation() == Biome.Precipitation.SNOW;
	}

	public static void addSnowUnderTrees(Biome biome) {
		Identifier id = BuiltinRegistries.BIOME.getId(biome);
		if (!biomesToAddTo.contains(id))
			biomesToAddTo.add(id);
	}

}
