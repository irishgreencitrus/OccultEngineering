# Phlogiport

The *Phlogiport* is a mid-to-late game logistics block that allows **wireless transmission of packages** to other
Phlogiports within range.
It operates on the same addressing system as Frogports, but does not interact with them directly.
The system is designed to **augment**, not replace, existing logistics tools by offering a compact, targeted delivery
option for higher-tier players.

- Accepts packages as input, and will wirelessly transmit them to the Phlogiport which matches the address on the
  package.
- Does not forward to Frogports
- The Phlogiport will forward packages that do not match its own address, provided it can find another Phlogiport that
  matches the address on the package.

This system works separately to the Frogports and Postboxes, and will need to have an item inserted into it for
it to work, rather than grabbing off a chain or a train.

However, it uses the same package addresses, so they are compatible systems.

## Crafting:

Silver Phlogistate which can be crafted using Afrit Blaze Heating from sterling silver and lava.
Something else more magical.
Use a ritual to craft 2 of them.
Fairly mid/late game in order to not completely obsolete Frogports.

## On place

When a Phlogiport is placed, it notifies the `PhlogiportNetworkHandler` that it has been placed, which
registers it to the network

## On destroy

When a Phlogiport is destroyed, the network will be notified of its destruction and no packages will be forwarded there
anymore.

## How it works

1. A package enters the Phlogiport.
2. If the package does not have a destination, we stop here.
3. The Phlogiport asks the `PhlogiportNetworkHandler` for a destination
   1. Exact address matches take priority.
   2. If not found, we look at wildcards.
   3. If we match multiple, we return a **random** phlogiport back.
4. The `PhlogiportNetworkHandler` sends the result back to the requesting block, unless:
   1. The destination Phlogiport's chunk is not loaded
   2. The destination Phlogiport is too far away (128 blocks by default).
5. If we do not get back a valid Phlogiport, it remains in the inventory until one becomes available (recheck every so
   often).
6. If we do get back a valid Phlogiport, play an animation and move the item to its inventory.

## Further Improvements

- **Portable Phlogiport** where you can request packages be delivered directly to a console in your inventory.
- **Add a 'Beam me up, Scotty' advancement** for crafting and using a Phlogiport.
- **Dimensional Phlogiport**: add a lategame block that allows items to be sent across dimensions. (it may also have to
  be a chunkloader)