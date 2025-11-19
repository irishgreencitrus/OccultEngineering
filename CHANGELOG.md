# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [beta/v1.21.1-0.10.2] - 2025-11-19
### :sparkles: New Features
- [`a51566a`](https://github.com/irishgreencitrus/OccultEngineering/commit/a51566a8704748fc9d29f830527d5b84a63c967a) - New Crowdin updates *(PR [#42](https://github.com/irishgreencitrus/OccultEngineering/pull/42) by [@irishgreencitrus](https://github.com/irishgreencitrus))*

### :wrench: Chores
- [`7966975`](https://github.com/irishgreencitrus/OccultEngineering/commit/79669759d9b2f29df6bb3d37fbcbf702735af234) - Update crowdin.yml *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`5de9b27`](https://github.com/irishgreencitrus/OccultEngineering/commit/5de9b27a8ce08535f9ddd94e335782dbba807353) - Revert changes to crowdin.yml *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


## [beta/v1.21.1-0.10.1] - 2025-11-16
### :sparkles: New Features
- [`6efcf01`](https://github.com/irishgreencitrus/OccultEngineering/commit/6efcf01270ff5508a787362e1fb19dbd052dbde9) - Add a unique model for the combined goggles. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`3053300`](https://github.com/irishgreencitrus/OccultEngineering/commit/30533001cabc5e94ebccb53069481dac51591dc1) - Add a keybind for toggling the combined goggles when they are on your head. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*

### :bug: Bug Fixes
- [`c097758`](https://github.com/irishgreencitrus/OccultEngineering/commit/c097758b465eb52a2d3fdf9dca58a7201844ea2f) - Allow the otherworld detector to depower and repower comparators properly. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


## [beta/v1.21.1-0.10.0] - 2025-11-11
### :boom: BREAKING CHANGES
- due to [`161ed7f`](https://github.com/irishgreencitrus/OccultEngineering/commit/161ed7fe093b6962f8e554fcc976fee840853e0c) - Update Create and Occultism dependencies. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*:

  Update Create and Occultism dependencies.


### :sparkles: New Features
- [`161ed7f`](https://github.com/irishgreencitrus/OccultEngineering/commit/161ed7fe093b6962f8e554fcc976fee840853e0c) - **dependencies**: Update Create and Occultism dependencies. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


## [beta/v1.21.1-0.9.2] - 2025-10-22
### :sparkles: New Features
- [`d72750b`](https://github.com/irishgreencitrus/OccultEngineering/commit/d72750b6739817200339f6aac33d153231ad43b8) - Add tooltip entries to the Mechanical Chamber. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`ca052ec`](https://github.com/irishgreencitrus/OccultEngineering/commit/ca052ec57d7d689ca395acecd1ab05ace4e50292) - Add display sources to the Mechanical Chamber. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


## [beta/v1.20.1-0.9.0] - 2025-10-10
Hello everyone, I'm sorry it's taken such a long time to make a new release of Create: Occult Engineering (like 4 months), but I've finally done one!

I spent about a month trying to come up with features that were over complex and not feasable for me to do at this time. The plan was to reinvent how Create's schematics work, but use them only for Pentacles, so you could place down any pentacle without needing to draw everything individually. I got 90% of the way there, but that meant I only completed 10% of the work as these things go.

Anyway, I took a break from the project for a few months and came up with a new idea that I actually managed to finish (although this still took me a while.

### Phlogiports!
Phlogiports are a new way to route Create's packages a short to medium amount of distance. When a named package enters a Phlogiport which has a different name, the Phlogiport checks within 128 blocks of it to find another with that name. If one is found, the package is forwarded wirelessly! If multiple are found, a random one is picked and the package is sent to that one.

These work on a separate network to Frogports and Trains, and only forward between each other.

For a (hopefully) better explanation, check its Ponder menu.
#### Crafting
Phlogiports can be crafted using a ritual from a Sterling Silver Block, a Gold Block, a bar of Silver Phlogistate and a transmitter!

For info about crafting the intermediate materials, refer to the Encyclopedia of Souls, JEI or the Ponder menu.

### Other Changes
#### Phlogiston
An intermediate material for crafting Phlogiports (and other things in the future!)

#### Silver Phlogistate
Another intermediate material for crafting Phlogiports (and other things in the future!)

#### Creative Menu
The Creative Menu has been re-ordered slightly, just thought I'd mention it.

#### Schematics Features
All of Occultism's and Occult Engineering's chalk can be used in schematics and get consumed properly.

#### Unfinished Features
As mentioned above, some features have been left in but are unfinished, these include Pentacle Schematics, the Púca mob (which is mostly functional, but has no purpose) and the workstations for using Pentacle Schematics.
I'm going to leave these in for now since they're not harming anyone.

### Final notes from me
I'm going to start porting Occult Engineering to 1.21.1 soon, and will release that first.
After that, I need feature requests!
Join me on the Discord, or make a GitHub feature request and if it's good and fits the mod's theme, I'll include it!

### :sparkles: New Features
- [`2e7f1da`](https://github.com/irishgreencitrus/OccultEngineering/commit/2e7f1daae675fdba9e74f30a5c235a14a868e574) - Making the Schematicannon work with chalks. Also moved a bunch of stuff around in prep for adding Pentacle Schematics. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`5b3fd52`](https://github.com/irishgreencitrus/OccultEngineering/commit/5b3fd52ded941a94b51f403c7e11e28ad5377fbe) - Start working on the Pentacle Altar. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`5685c09`](https://github.com/irishgreencitrus/OccultEngineering/commit/5685c097d9ac2ea18bec1d465a7b13efa2d427e3) - Continue the work on the Pentacle printing system. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`100c395`](https://github.com/irishgreencitrus/OccultEngineering/commit/100c3952410235608accd81e9e0b6c9e8e406f14) - Start work on the Púcalith. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`73199de`](https://github.com/irishgreencitrus/OccultEngineering/commit/73199de9c80e784a968e2b519357d0e98edbef52) - Make the Pentacle Altar only display valid pentacles, rather than every multiblock. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`e8d2d9f`](https://github.com/irishgreencitrus/OccultEngineering/commit/e8d2d9f00f3934e182b9ed6cfd9cda77ddccdb1d) - Complete the Pentacle Material Checklist. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`84036e5`](https://github.com/irishgreencitrus/OccultEngineering/commit/84036e538ccd10da431111d713135e2a5c93f670) - Add the Púca mob *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`67fd83b`](https://github.com/irishgreencitrus/OccultEngineering/commit/67fd83bd58a98ebf704f4664d61925851bc5f2f5) - Give the Púca a brain *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`5d5dac1`](https://github.com/irishgreencitrus/OccultEngineering/commit/5d5dac1c78567447bcb2c5107c1aad850c386287) - Make the Púca hop around and give it a spawn egg *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`28730c2`](https://github.com/irishgreencitrus/OccultEngineering/commit/28730c2378a07e9c51dcf6d1e34c6a9784964fb4) - Rework a few bits around the Pentacle printing system. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`a9ed468`](https://github.com/irishgreencitrus/OccultEngineering/commit/a9ed4680545375c9279886697bb2396def98f786) - Pentacle Printing work & Dynamic Brains *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`a79f617`](https://github.com/irishgreencitrus/OccultEngineering/commit/a79f61746cd0ec4328bd41960a0e1eb173f57a7a) - Start work on the Púca being able to place blocks. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`72c5c2b`](https://github.com/irishgreencitrus/OccultEngineering/commit/72c5c2b67c4a6ec1e094157672c14c5ec1c38c0a) - Add insta-place to the PentaclePrinter *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`5048cc6`](https://github.com/irishgreencitrus/OccultEngineering/commit/5048cc61978e854dc02329e1585ff6ea96714692) - Update Registrate url *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`1167ff8`](https://github.com/irishgreencitrus/OccultEngineering/commit/1167ff8fad3144cafe2fdd28fd8163bd1dddc7f8) - Remove stuff related to the Pucalith actually using a Puca entity. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`4c6a7bb`](https://github.com/irishgreencitrus/OccultEngineering/commit/4c6a7bbedff8d8d5a4ab34d4a3e6e986222b517e) - Implement the Phlogiport *(PR [#22](https://github.com/irishgreencitrus/OccultEngineering/pull/22) by [@irishgreencitrus](https://github.com/irishgreencitrus))*

### :bug: Bug Fixes
- [`edfafb1`](https://github.com/irishgreencitrus/OccultEngineering/commit/edfafb1d486417c81328b9336f0b927f8d5dd7ec) - Make the PentacleAltar save its inventory to NBT. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`41cd6da`](https://github.com/irishgreencitrus/OccultEngineering/commit/41cd6da71775de0de0395d5c7640cea1c867482e) - Fix bugs in PentacleMaterialChecklist and remove a rogue logging statement *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`7f00880`](https://github.com/irishgreencitrus/OccultEngineering/commit/7f0088076814a8e70d92b406b872ff2e6a97e0e0) - Fix runData by scrapping Registrate for entities *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*

### :wrench: Chores
- [`c593cf6`](https://github.com/irishgreencitrus/OccultEngineering/commit/c593cf68a06456976991b5d2e0c2d78252c4b568) - Don't forget about mixins.json *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`0d06087`](https://github.com/irishgreencitrus/OccultEngineering/commit/0d0608782dd675b89aef80dfd40dae6f0bdb25ae) - Cleanup the material checklist code. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


# OCCULT ENGINEERING v0.9.0
## [beta/v1.20.1-0.8.1] - 2025-05-24
### :bug: Bug Fixes
- [`d56c63b`](https://github.com/irishgreencitrus/OccultEngineering/commit/d56c63b8171d0c04b3ae619b2be4697b34748991) - Fix Javadoc from failing my build (thanks Gradle) *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


## [beta/v1.20.1-0.7.0] - 2025-04-14
### :sparkles: New Features
- [`8072514`](https://github.com/irishgreencitrus/OccultEngineering/commit/8072514b68fd7b8d88090529fb5f17d1318f54d7) - Add assets for Sterling Silver ingot and nuggets. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`ab00e1e`](https://github.com/irishgreencitrus/OccultEngineering/commit/ab00e1ee66bc24134b14c01fb73221a4bcc69a09) - Add Sterling Silver block and recipes *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`8523d71`](https://github.com/irishgreencitrus/OccultEngineering/commit/8523d717cb7e1378d8b75b7d9114248365aaba70) - Update the Mechanical Chamber's model *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`831f47c`](https://github.com/irishgreencitrus/OccultEngineering/commit/831f47c9b5ac5fc0097286c6e3771a2fc6de73de) - Add the new Modonomicon and misc. changes. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`eb04531`](https://github.com/irishgreencitrus/OccultEngineering/commit/eb045314498f3bd4750c607be7413ce9b0e66e49) - Add a shaft to the bottom of the Mechanical Chamber *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`c0297fa`](https://github.com/irishgreencitrus/OccultEngineering/commit/c0297fa90430aed69d1ade0569f197be6a41a95c) - Outline the Encyclopedia of Spirits *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`36cb872`](https://github.com/irishgreencitrus/OccultEngineering/commit/36cb87235fc1e6868085e9ef0ac4951036f256cb) - Finish the Encyclopedia of Souls, for now. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


## [beta/v1.20.1-0.6.0] - 2025-03-15
### :sparkles: New Features
- [`6b812cf`](https://github.com/irishgreencitrus/OccultEngineering/commit/6b812cfdaf30404828f320bd0226b42618df55d5) - Implement manual interaction for the Mechanical Pulverizer. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`0951fcd`](https://github.com/irishgreencitrus/OccultEngineering/commit/0951fcdbe1f9021116a962749b964c9217c9b657) - Add a ponder scene for the Mechanical Pulverizer. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`eca7c69`](https://github.com/irishgreencitrus/OccultEngineering/commit/eca7c693444ac694ef03535057090987c56d8310) - Add a ponder scene for the Mechanical Chamber *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`f861f97`](https://github.com/irishgreencitrus/OccultEngineering/commit/f861f9791fc76177a47d192c04d91932522cc136) - Add purple for Spirit Solution and the ability to generate Otherstone from touching lava *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`1f0c603`](https://github.com/irishgreencitrus/OccultEngineering/commit/1f0c603cee57ea0b8d6a375b0318cef0a544eba9) - Add ponder scene for the Otherworld Detector *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*

### :bug: Bug Fixes
- [`4642875`](https://github.com/irishgreencitrus/OccultEngineering/commit/46428758df1c449251eebf425fe62f703e93ec5d) - Slightly change timings on the Mechanical Chamber's ponder scenes to be more clear *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


## [beta/v1.20.1-0.5.0] - 2025-03-12
### :sparkles: New Features
- [`5b4c27a`](https://github.com/irishgreencitrus/OccultEngineering/commit/5b4c27a9c9a6c70353b9911e8dc77b7feb1b1684) - Add Mechanical Pulverizer *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`11b3a98`](https://github.com/irishgreencitrus/OccultEngineering/commit/11b3a98ea0084a13d29b2483fae06d69bbf7e4c9) - Add proper processing particles to pursue perfecting pulverization *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`7095735`](https://github.com/irishgreencitrus/OccultEngineering/commit/7095735a555491fe3acc0599a01825568593ed69) - Add Zinc & Brass dusts which are used to craft the mod's chalks. *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*
- [`299ab8e`](https://github.com/irishgreencitrus/OccultEngineering/commit/299ab8e8b2a1fa0e854c91a1382678607ca53e8e) - Add crafting recipe for Mechanical Pulverizer *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


## [beta/v1.20.1-0.4.1] - 2025-03-09
### :bug: Bug Fixes
- [`ad8ec4a`](https://github.com/irishgreencitrus/OccultEngineering/commit/ad8ec4a2ee063e7265a5d2e912296ddfb9c0ab64) - Make sure artifact gets proper name (Hopefully last of GitHub actions changes) *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*


## [beta/v1.20.1-0.4.0] - 2025-03-09
### :bug: Bug Fixes
- [`f8783ed`](https://github.com/irishgreencitrus/OccultEngineering/commit/f8783ed651adf461440b3238c08ff5289a14b9bf) - Update GitHub actions, to add changelog *(commit by [@irishgreencitrus](https://github.com/irishgreencitrus))*

[beta/v1.20.1-0.4.0]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.20.1-0.3.0...beta/v1.20.1-0.4.0
[beta/v1.20.1-0.4.1]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.20.1-0.4.0...beta/v1.20.1-0.4.1
[beta/v1.20.1-0.5.0]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.20.1-0.4.1...beta/v1.20.1-0.5.0
[beta/v1.20.1-0.6.0]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.20.1-0.5.0...beta/v1.20.1-0.6.0
[beta/v1.20.1-0.7.0]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.20.1-0.6.0...beta/v1.20.1-0.7.0
[beta/v1.20.1-0.8.1]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.20.1-0.8.0...beta/v1.20.1-0.8.1
[beta/v1.20.1-0.9.0]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.20.1-0.8.1...beta/v1.20.1-0.9.0
[beta/v1.21.1-0.9.2]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.21.1-0.9.1...beta/v1.21.1-0.9.2
[beta/v1.21.1-0.10.0]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.21.1-0.9.2...beta/v1.21.1-0.10.0
[beta/v1.21.1-0.10.1]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.21.1-0.10.0...beta/v1.21.1-0.10.1
[beta/v1.21.1-0.10.2]: https://github.com/irishgreencitrus/OccultEngineering/compare/beta/v1.21.1-0.10.1...beta/v1.21.1-0.10.2
