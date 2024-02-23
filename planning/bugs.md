adjacent matches of different types get attached incorrectly:
`<item:diamond> <card:base1-4>` goes to `<item:diamond> [diamond item]`
^^ seems like it was being caused by the item renderer not rendering, which seems,, bad