# Adaptation Mohist 1.20.1 (Geoventure)

Ce dépôt est un fork de [`Warfactory-Official/Golem-Core`](https://github.com/Warfactory-Official/Golem-Core) (licence **LGPL-3.0**, auteur original **Golem**), basé sur la version `v1.0.6`. Les licences [`LICENSE`](LICENSE), [`LICENSE.LESSER`](LICENSE.LESSER) et [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md) d'origine sont conservées intégralement.

## Problème corrigé

Sous serveur hybride **Mohist 1.20.1** (Forge 47.4.x + CraftBukkit/Spigot), le patch serveur sur `PlayerList.respawn(ServerPlayer, boolean)` :

1. Remplace l'appel vanilla/Forge `MinecraftServer.overworld()` (`m_129783_()`) par `MinecraftServer.getLevel(Level.OVERWORLD)` (`m_129880_(Level.f_46428_)`).
2. Ajoute deux surcharges Bukkit nommées `respawn(...)`.

En conséquence, le mixin upstream `dev.golem.core.mixin.MixinPlayerListRespawns` (qui exige `MinecraftServer;overworld()`) provoque une erreur d'injection Mixin au chargement puis un `NoClassDefFoundError` sur `PlayerList`.

## Correctif retenu

Plutôt que de simplement désactiver `MixinPlayerListRespawns` sous Mohist (ce qui neutraliserait `Respawns.RULES`, `Respawns.FALLBACKS` et le contrôle `WorldAttributes.BED_SPAWN` au respawn sur un lit sauvegardé), ce dépôt :

- Ajoute [`dev.golem.core.compat.MohistCompatPlugin`](src/main/java/dev/golem/core/compat/MohistCompatPlugin.java), enregistré comme `plugin` dans [`golemcore.mixins.json`](src/main/resources/golemcore.mixins.json).
- Ajoute [`dev.golem.core.mixin.MixinPlayerListRespawnsMohist`](src/main/java/dev/golem/core/mixin/MixinPlayerListRespawnsMohist.java) (actif uniquement sur Forge `< 1.21` lorsque Mohist est détecté) qui cible la signature explicite `respawn(Lnet/minecraft/server/level/ServerPlayer;Z)Lnet/minecraft/server/level/ServerPlayer;` et remplace le point d'injection `overworld()` par le second appel (`ordinal = 1`) à `MinecraftServer.getLevel(ResourceKey)`.
- Conserve 100 % du comportement upstream intact sous client Forge pur, serveur Forge pur, Fabric et NeoForge.

## Construction reproductible

La construction multi-cibles Stonecutter est exécutée en intégration continue via `.github/workflows/prerelease.yml` (`./gradlew chiseledBuild`). En local (sans blocage TLS externe sur `maven.kikugie.dev`), lancer :

```bash
CI=false ./gradlew chiseledBuild --no-daemon --stacktrace
```
