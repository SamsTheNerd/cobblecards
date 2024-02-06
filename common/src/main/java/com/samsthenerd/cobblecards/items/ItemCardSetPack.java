package com.samsthenerd.cobblecards.items;

// public class ItemCardSetPack extends ItemCardPack implements IDynamicModelItem {

//     public ItemCardSetPack(Settings settings) {
//         super(settings);
//     }
    
//     public DynamicModelOverride getModelIdentifier(ItemStack stack){
//         CardSet set = getSet(stack);
//         if(set == null) return null;
//         return DynamicModelOverride.fromTextures(List.of(new Identifier(CobbleCards.MOD_ID, "item/packs/" + set.id)), new Identifier("cobblecards", "packs/" + set.id));
//     }

//     public ItemStack fromSet(CardSet set){
//         ItemStack stack = new ItemStack(this);
//         stack.getOrCreateNbt().putString("set", set.id);
//         return stack;
//     }

//     public CardSet getSet(ItemStack stack){
//         if(stack.hasCustomName()){
//             return CardSet.get(stack.getName().getString());
//         }
//         if(stack.hasNbt() && stack.getNbt().contains("set")){
//             CardSet.get(stack.getNbt().getString("set"));
//         }
//         return null;
//     }

//     public Set<Card> possibleCards(ItemStack stack){
//         CardSet set = getSet(stack);
//         if(set != null){
//             return set.getCards();
//         }
//         return null;
//     }

//     public List<Card> getRandomizedCards(ItemStack stack, ServerPlayerEntity player){
//         CardSet set = getSet(stack);
//         if(set != null){
//             PullManager pullManager = new PullManager(player);
//             return pullManager.pullCards(set.getCards(), List.of(PullSlot.COMMON, PullSlot.RARE, PullSlot.REV_HOLO, PullSlot.BASIC_ENERGY, PullSlot.UNCOMMON));
//         }
//         return null;
//     }

//     public int getPackSize(ItemStack stack){
//         return 10;
//     }

//     // allows it to wait for other stuff before it opens
//     public boolean readyToOpen(ItemStack stack){
//         return getSet(stack) != null;
//     }

//     public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//         ItemStack stack = user.getStackInHand(hand);
//         CardSet set = getSet(stack);
//         return super.use(world, user, hand);
//     }

//     @Override
//     public Optional<TooltipData> getTooltipData(ItemStack stack) {
//         CardSet set = getSet(stack);
//         if(set != null){
//             return Optional.of(new URLImageTooltipData(
//                 new Identifier("cobblecards", "pokeset/" + set.id),
//                 set.getLogoUrl(),
//                 (w, h) -> { return 96; }
//             ));
//         }
//         return Optional.empty();
//     }
// }
