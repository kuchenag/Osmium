package com.intro.config;

import com.intro.config.options.*;
import com.intro.render.Colors;

import java.util.HashMap;

/**
 * The Options class stores all Osmium options.
 * For referencing options, options are stored in a normal variable form.
 * Options are also stored in the options HashMap, which contains a cache of all options.
 * When options are saved, the options are de-cached from the hashmap and loaded into their corresponding variables.
 * When options are loaded, options are deserialized from json form, and loaded into the hashmap.
 *
 *
 * @since 1.0
 * @author Intro
 */
public class Options {

    /**
     * Cache of all options
     */
    private final HashMap<String, Option> options = new HashMap<>();

    public Option get(String identifier) {
        return options.get(identifier);
    }

    public BooleanOption getBooleanOption(String identifier) {
        return (BooleanOption) get(identifier);
    }

    public EnumOption getEnumOption(String identifier) {
        return (EnumOption) get(identifier);
    }

    public DoubleOption getDoubleOption(String identifier) {
        return (DoubleOption) get(identifier);
    }

    public Vector2Option getVector2Option(String identifier) {
        return (Vector2Option) get(identifier);
    }

    public ColorOption getColorOption(String identifier) {
        return (ColorOption) get(identifier);
    };


    public void put(String identifier, Option option) {
        options.put(identifier, option);
    }


    public Option ToggleSprintEnabled;
    public Option FullbrightEnabled;
    public Option HurtbobbingEnabled;
    public Option SneakMode;
    public Option NoRainEnabled;
    public Option FpsEnabled;
    public Option CustomCapeMode;
    public Option NoFireEnabled;
    public Option ToggleSprintPosition;
    public Option FpsDisplayPosition;
    public Option ToggleSneakEnabled;
    public Option FireworksDisabled;
    public Option FlyBoostAmount;
    public Option FlyBoostEnabled;
    public Option DecreaseNetherParticles;
    public Option BlockOutlineMode;
    public Option BlockOutlineColor;
    public Option BlockOutlineAlpha;
    public Option StatusEffectDisplayEnabled;
    public Option StatusEffectDisplayPosition;



    public void init() {
        // Default inits
        // Changed to preferences when config is loaded
        this.setDefaults();
    }

    /**
     * Caches the options HashMap using the corresponding variables.
     * Catch block is to check if anything is null, and will set all options to default.
     */
    public void putHashMap() {

            put(ToggleSprintEnabled.identifier, ToggleSprintEnabled);
            put(FullbrightEnabled.identifier, FullbrightEnabled);
            put(HurtbobbingEnabled.identifier, HurtbobbingEnabled);
            put(SneakMode.identifier, SneakMode);
            put(NoRainEnabled.identifier, NoRainEnabled);
            put(FpsEnabled.identifier, FpsEnabled);
            put(CustomCapeMode.identifier, CustomCapeMode);
            put(NoFireEnabled.identifier, NoFireEnabled);
            put(ToggleSprintPosition.identifier, ToggleSprintPosition);
            put(FpsDisplayPosition.identifier, FpsDisplayPosition);
            put(ToggleSneakEnabled.identifier, ToggleSneakEnabled);
            put(FireworksDisabled.identifier, FireworksDisabled);
            put(FlyBoostAmount.identifier, FlyBoostAmount);
            put(FlyBoostEnabled.identifier, FlyBoostEnabled);
            put(DecreaseNetherParticles.identifier, DecreaseNetherParticles);
            put(BlockOutlineMode.identifier, BlockOutlineMode);
            put(BlockOutlineColor.identifier, BlockOutlineColor);
            put(BlockOutlineAlpha.identifier, BlockOutlineAlpha);
            put(StatusEffectDisplayEnabled.identifier, StatusEffectDisplayEnabled);
            put(StatusEffectDisplayPosition.identifier, StatusEffectDisplayPosition);

    }

    /**
     * De-caches the options HashMap into its corresponding variables.
     */
    public void getHashMap() {
        ToggleSprintEnabled = get(ToggleSprintEnabled.identifier);
        FullbrightEnabled = get(FullbrightEnabled.identifier);
        HurtbobbingEnabled = get(HurtbobbingEnabled.identifier);
        SneakMode = get(SneakMode.identifier);
        NoRainEnabled = get(NoRainEnabled.identifier);
        FpsEnabled = get(FpsEnabled.identifier);
        CustomCapeMode = get(CustomCapeMode.identifier);
        NoFireEnabled = get(NoFireEnabled.identifier);
        ToggleSprintPosition = get(ToggleSprintPosition.identifier);
        FpsDisplayPosition = get(FpsDisplayPosition.identifier);
        ToggleSneakEnabled = get(ToggleSneakEnabled.identifier);
        FireworksDisabled = get(FireworksDisabled.identifier);
        FlyBoostAmount = get(FlyBoostAmount.identifier);
        FlyBoostEnabled = get(FlyBoostEnabled.identifier);
        DecreaseNetherParticles = get(DecreaseNetherParticles.identifier);
        BlockOutlineMode = get(BlockOutlineMode.identifier);
        BlockOutlineColor = get(BlockOutlineColor.identifier);
        BlockOutlineAlpha = get(BlockOutlineAlpha.identifier);
        StatusEffectDisplayEnabled = get(StatusEffectDisplayEnabled.identifier);
        StatusEffectDisplayPosition = get(StatusEffectDisplayPosition.identifier);
    }

    /**
     * Assigns the default settings to the option variables.
     */
    public void setDefaults() {
        ToggleSprintEnabled = new BooleanOption( "ToggleSprintEnabled", false);
        FullbrightEnabled = new BooleanOption("FullBrightEnabled", false);
        HurtbobbingEnabled = new BooleanOption("HurtBobbingEnabled", false);
        SneakMode = new EnumOption("SneakMode", com.intro.config.options.SneakMode.VANILLA);
        NoRainEnabled = new BooleanOption("NoRainEnabled", false);
        FpsEnabled = new BooleanOption("FpsEnabled", false);
        CustomCapeMode = new EnumOption( "CustomCapeMode", CapeRenderingMode.DISABLED);
        NoFireEnabled = new BooleanOption("NoFireEnabled", false);
        ToggleSprintPosition = new Vector2Option("ToggleSprintPosition", 5, 5);
        FpsDisplayPosition = new Vector2Option("FpsDisplayPosition", 5, 5);
        ToggleSneakEnabled = new BooleanOption("ToggleSneakEnabled", false);
        FireworksDisabled = new BooleanOption("FireworksDisabled", false);
        FlyBoostAmount = new DoubleOption("FlyBoostAmount", 1D);
        FlyBoostEnabled = new BooleanOption("FlyBoostEnabled", false);
        DecreaseNetherParticles = new BooleanOption("DecreaseNetherParticles", false);
        BlockOutlineMode = new EnumOption("CustomBlockOutline", com.intro.config.options.BlockOutlineMode.VANILLA);
        BlockOutlineColor = new ColorOption("BlockOutlineColor", Colors.TRANSPARENT.getColor());
        BlockOutlineAlpha = new DoubleOption("BlockOutlineAlpha", 1);
        StatusEffectDisplayEnabled = new BooleanOption("StatusEffectDisplayEnabled", false);
        StatusEffectDisplayPosition = new Vector2Option("StatusEffectDisplayPosition", 0, 0);
    }

    public HashMap<String, Option> getValues() {
        return this.options;
    }
}