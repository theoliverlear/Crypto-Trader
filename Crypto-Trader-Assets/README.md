# Crypto Trader — Assets Module
## Inward‑facing, curated assets and loaders powering a consistent Crypto Trader experience

---

Crypto‑Trader‑Assets is an inward‑facing module at the core of the platform. It
curates brand visuals (logos, icons) and provides zero‑boilerplate loaders that
hand back ready‑to‑use JavaFX `Image` objects. By centralizing assets and 
access patterns, it keeps the entire product visually consistent, faster to
build, and easier to govern. It is not a public entry point; it exists to 
power the rest of Crypto Trader.

Important: Past results and backtests do not guarantee future performance. 
Always start in paper mode. Liability is your own.

### What this module is
- A single source of truth for shared UI assets (today: images; designed to 
  also host stylesheets/scripts where appropriate).
- Helper APIs that turn resource paths into ready‑to‑use JavaFX objects in one
  line.
- A controlled access layer: Java Module exports only expose intended 
  packages.

## ⭐️ What it does
- Centralized assets
  - Stores shared images under `src/main/resources/assets/...` with stable,
    documented paths.
- One‑line loading helpers
  - Typed classes (e.g., `CryptoTraderLogoImageAssets`, `IconImageAssets`)
    expose ready‑to‑use JavaFX `Image` constants.
  - Generic utilities (e.g., `ImageResource`) load images by path without 
    repeating `getResourceAsStream` boilerplate.
- Access boundaries
  - Java Module exports restrict what external apps can use, keeping internal
    details private and usage consistent.
- Deduplication and consistency
  - Eliminates copy‑pasted asset files and ad‑hoc loaders across modules.

## ✅ Why it matters
- Consistency: one brand, one icon set, one path convention across apps.
- Speed: grab a logo or icon with a single constant — no fragile classpath 
  plumbing.
- Governance: module exports and a dedicated jar make access intentional and 
  reviewable.
- Maintainability: change an asset once; all consumers see the update.

## 🔗 Where it shows up across the platform
- Consumed by desktop modules (e.g., Admin) for JavaFX images.
- Referenced by Docs for visuals and path conventions.
- Complements web assets (Angular uses its own build pipeline but mirrors path
  conventions).

## 🚀 How it works (at a glance)
1. Assets live in the jar under `/assets/...` (e.g., `/assets/images/...`).
2. Helper classes expose typed constants or provide utility loaders.
3. Consumers import the module and use constants or helpers to retrieve 
   `Image` objects.

## 🧰 Internal usage examples
- Use a Crypto Trader logo in JavaFX:

```java
import javafx.scene.image.ImageView;
import org.cryptotrader.assets.images.logos.cryptotrader.fx.CryptoTraderLogoImageAssets;

ImageView logo = new ImageView(CryptoTraderLogoImageAssets.CROPPED_TRANSPARENT_PNG);
```

- Use a shared icon (SVG converted by `javafxsvg` at runtime):

```java
import javafx.scene.image.ImageView;
import org.cryptotrader.assets.images.icons.fx.IconImageAssets;

ImageView exitIcon = new ImageView(IconImageAssets.EXIT_DOOR_SVG);
```

- Load any image generically by path:

```java
import javafx.scene.image.Image;
import org.cryptotrader.assets.util.ImageResource;

ImageResource res = new ImageResource();
Image img = res.getImage("/assets/images/icons/stock_icon.svg");
```

## 🔒 Access, safety, and control
- Inward-facing: consumed by first‑party Crypto Trader apps and services; not
  a public entry point.
- Use the exported APIs rather than reaching into resource files directly.
- Controlled exports: only packages listed in `module-info.java` are 
  accessible; usage is intentional and reviewable.
- No secrets/config here — this module carries public, non‑sensitive UI assets
  only.
- Change management: updates to shared assets are reviewed to protect brand 
  consistency and UX.

## 🛠️ Technology at a glance
- Java + JavaFX (`javafx-controls`, `javafx-fxml`, `javafx-web`, 
  `javafx-swing`)
- `javafxsvg` for SVG rendering into JavaFX images
- UI libraries included for alignment with platform visuals: ControlsFX, 
  FormsFX, ValidatorFX, Ikonli, BootstrapFX, TilesFX
- Lombok (provided), JUnit Jupiter (tests), Dokka (docs)

## 💡 What it unlocks for Crypto Trader
- One brand, everywhere: shared logos and icons keep Admin, web, and docs 
  visually cohesive.
- Faster feature work: ready-to-use JavaFX `Image` constants eliminate UI 
  plumbing.
- Single source of truth: update one asset, the whole platform reflects it.
- Safer access and governance: controlled module exports make usage 
  intentional and reviewable.
- Observability of change: asset diffs are centralized and easy to roll back.
- Less duplication: no scattered copies of files or loaders across modules.
- Reliable rendering: standardized loaders and SVG handling reduce production 
  surprises.

## 📝 Conventions
- Keep filenames lowercase with underscores; prefer descriptive names.
- Add shared assets under `src/main/resources/assets/...` in a logical folder
  (e.g., `images/icons`, `images/logos/...`).
- Prefer provided constants (`...ImageAssets`) over ad‑hoc loaders.
- Submit changes that improve clarity and reuse; avoid app‑specific or secret 
  content here.

## ❓ Questions or help
Email Oliver Lear Sigwarth (@theoliverlear): sigwarthsoftware@gmail.com

## 📄 License
See `LICENSE.md` in the repository root.
