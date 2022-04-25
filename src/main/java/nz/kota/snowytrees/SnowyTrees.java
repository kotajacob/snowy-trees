package nz.kota.snowytrees;

import java.util.List;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;

public class SnowyTrees implements ModInitializer {
	private static final Feature<DefaultFeatureConfig> SNOWY_TREES = new SnowUnderTreeFeature(DefaultFeatureConfig.CODEC);

	public static ConfiguredFeature<?, ?> SNOWY_TREES_CONFIGURED = new ConfiguredFeature<>(SNOWY_TREES,
			FeatureConfig.DEFAULT);

	public static PlacedFeature SNOWY_TREES_PLACED = new PlacedFeature(RegistryEntry.of(SNOWY_TREES_CONFIGURED),
			List.of(BiomePlacementModifier.of()));

	@Override
	public void onInitialize() {
		Registry.register(Registry.FEATURE, new Identifier("kota", "snowytrees"), SNOWY_TREES);

		Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;
		RegistryKey<ConfiguredFeature<?, ?>> snowyTrees = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
				new Identifier("kota", "snowytrees"));
		Registry.register(registry, snowyTrees.getValue(), SNOWY_TREES_CONFIGURED);

		Registry<PlacedFeature> placedRegistry = BuiltinRegistries.PLACED_FEATURE;
		Registry.register(placedRegistry, snowyTrees.getValue(), SNOWY_TREES_PLACED);

		BiomeModifications.addFeature(b -> shouldAddSnow(b.getBiome()), GenerationStep.Feature.TOP_LAYER_MODIFICATION,
				BuiltinRegistries.PLACED_FEATURE.getKey(SNOWY_TREES_PLACED).get());

	}

	private boolean shouldAddSnow(Biome biome) {
		return biome.getPrecipitation() == Biome.Precipitation.SNOW;
	}
}
