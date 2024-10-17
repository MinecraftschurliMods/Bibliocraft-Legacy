# Bibliocraft Legacy

[Bibliocraft Legacy](https://www.curseforge.com/minecraft/mc-mods/bibliocraft-legacy) is a port of the original
Bibliocraft Minecraft mod by Nuchaz. It adds various pieces of decorative and functional furniture into the game.

For the original mod, see the [CurseForge page](https://www.curseforge.com/minecraft/mc-mods/bibliocraft) and
the [GitHub repo](https://github.com/Nuchaz/BiblioCraft-Source/tree/1.18.x).

## Developing Addons

Bibliocraft Legacy offers an API for other modders to integrate with. This is mainly intended to allow integration with
modded wood types.

### Setup

To get started, add Bibliocraft as a dependency for your mod in your `build.gradle`:

```groovy
repositories {
    // We currently do not host Bibliocraft on our own maven, this will change soon!
    maven { url 'https://cursemaven.com' }
}

dependencies {
    // At compile time, depend only on the API JAR. TODO actually make this an API jar.
    compileOnly "curse.maven:bibliocraft-legacy-1122260:5817492"
    // At runtime, use the full JAR.
    runtimeOnly "curse.maven:bibliocraft-legacy-1122260:5817492"
}
```

Next, add the dependency block in your `neoforge.mods.toml` file. Note: It is crucial that your mod is set to load
`BEFORE` Bibliocraft, to ensure that your event handlers will be fired early enough.

```toml
[[dependencies.${mod_id}]]
modId="bibliocraft"
type="optional"
versionRange="[1.1.0,)"
ordering="BEFORE"
side="BOTH"
```

Now, for the actual coding part. You should isolate all Bibliocraft-related logic in a separate class, similar to what
you'd do with client classes, so that it doesn't get classloaded when Bibliocraft isn't present. This can be done like
so:

```java
@Mod("yourmodid")
public class YourMod {
    // We need the bus parameter to pass to our compat class.
    public YourMod(IEventBus bus) {
        // other stuff here
        if (ModList.get().isLoaded("bibliocraft")) {
            BibliocraftCompat.setup(bus);
        }
    }
}

public class BibliocraftCompat {
    public static void setup(IEventBus bus) {
    }
}
```

### Adding Your Own Wood Types

To add your custom wood type, you must register it to `RegisterBibliocraftWoodTypesEvent` like so:

```java
public class BibliocraftCompat {
    public static void setup(IEventBus bus) {
        bus.addListener(BibliocraftCompat::registerWoodTypes);
    }
    
    private static void registerWoodTypes(RegisterBibliocraftWoodTypesEvent event) {
        // Register your wood type(s) here.
        event.register(
                // The id of your wood type. This should be your mod id and the wood type's "name".
                ResourceLocation.fromNamespaceAndPath("yourmodid", "yourwoodtype"),
                // An instance of the vanilla WoodType class. This mainly defines sound-related properties.
                // Can be your own wood type object, but you can also reuse a vanilla WoodType if you want.
                ModWoodTypes.YOUR_WOOD_TYPE,
                // A supplier for the block properties that your wood type's furniture should use.
                // Typically, you want to copy your planks' properties for this.
                () -> BlockBehavior.Properties.ofFullCopy(ModBlocks.YOUR_WOODEN_PLANKS.get()),
                // The location of your planks' texture. This will be used by datagen, see below.
                ResourceLocation.fromNamespaceAndPath("yourmodid", "block/yourwoodtype_planks"),
                // A supplier for a block family for the wooden planks and related blocks. At the very least,
                // this should contain slab and stair blocks. This will also be used by datagen.
                // If applicable, you can reuse block families you already use for datagen.
                () -> new BlockFamily.Builder(ModBlocks.YOUR_WOODEN_PLANKS.get())
                        .slab(ModBlocks.YOUR_WOODEN_SLAB.get())
                        .stairs(ModBlocks.YOUR_WOODEN_STAIRS.get())
                        .getFamily()
        );
    }
}
```

And that's it! Now, if you boot up your game, you will notice that Bibliocraft will have blocks in your wood type's
variant, however they will not have models, textures or any other data. For that, we need to set up datagen. And this
is where things become a little tricky.

Due to how Minecraft's datagen works, it is not possible to have multiple data providers write to the same file. This
means that we have to capture the language provider and the two tags providers and pass them to our providers directly,
due to how Bibliocraft's datagen is structured internally.

With language providers, this works without issues. However, Mojang programmed the tags providers to auto-clear before
running their `#addTags` method. This is bad for us because it means we will lose tag entries.

As a workaround, the Bibliocraft API provides you with the `NonClearingBlockTagsProvider` and
`NonClearingItemTagsProvider` classes. These classes are modified to not clear the existing values before generating,
and can be used as a replacement for `BlockTagsProvider`/`ItemTagsProvider` in your tag datagen classes.

Tying it all together, a Bibliocraft-enabled datagen would look something like this:

```java
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = "yourmodid")
public final class YourModDatagen {
    @SubscribeEvent
    private static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // First, store your language provider, your block tags provider, and your item tags provider as local fields.
        // Datagen for languages other than US English is currently not supported.
        LanguageProvider languageProvider = generator.addProvider(event.includeClient(),
                new YourModEnglishLanguageProvider(output, "yourmodid", "en_us"));
        BlockTagsProvider blockTagsProvider = generator.addProvider(event.includeServer(),
                new YourModBlockTagsProvider(output, lookupProvider, "yourmodid", existingFileHelper));
        ItemTagsProvider itemTagsProvider = generator.addProvider(event.includeServer(),
                new YourModItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), "yourmodid", existingFileHelper));
        // Get the datagen helper and the wood type registry.
        BibliocraftDatagenHelper helper = BibliocraftApi.getDatagenHelper();
        BibliocraftWoodTypeRegistry registry = BibliocraftApi.getWoodTypeRegistry();
        // Add your wood types.
        helper.addWoodTypeToGenerate(registry.get(ResourceLocation.fromNamespaceAndPath("yourmodid", "yourwoodtype")));
        helper.addWoodTypeToGenerate(registry.get(ResourceLocation.fromNamespaceAndPath("yourmodid", "yourwoodtype2")));
        // Alternatively, add all your wood types at once.
        helper.addWoodTypesToGenerateByModid("yourmodid");
        // Actually generate the files.
        helper.generateAll("yourmodid", event, languageProvider, blockTagsProvider, itemTagsProvider);
    }
}
```

Running this will output `assets` and `data` in both Bibliocraft's and your own mod's namespaces. This is expected and
no reason to worry, as this is required by Bibliocraft's internal structure.

### Adding Custom Lock Behaviors

Bibliocraft adds the Lock and Key item, which allows lockable block entities to be locked and unlocked with a named Lock
and Key. If your mod adds lockable blocks, you can add support for the Lock and Key like so:

```java
public class BibliocraftCompat {
    public static void setup(IEventBus bus) {
        bus.addListener(BibliocraftCompat::registerLockAndKeyBehaviors);
    }

    private static void registerLockAndKeyBehavior(RegisterLockAndKeyBehaviorEvent event) {
        // Register your behaviors here.
        event.register(
                // The class of the block entity to be locked.
                YourBlockEntity.class,
                // The getter for your block entity's LockCode.
                YourBlockEntity::getLockCode,
                // The setter for your block entity's LockCode.
                YourBlockEntity::setLockCode,
                // The getter for your block entity's name. This will be used when preventing players
                // to open the block entity, in the "%s is locked!" status message.
                YourBlockEntity::getDisplayName
        );
    }
}
```
