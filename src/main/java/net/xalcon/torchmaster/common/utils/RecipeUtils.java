package net.xalcon.torchmaster.common.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class RecipeUtils
{
    public static void add(ResourceLocation recipeName, ItemStack output, String[] pattern, Tuple<Character, ? extends Ingredient>... ingredients)
    {
        int w = Arrays.stream(pattern).map(String::length).max(Comparator.naturalOrder()).orElse(0);
        int h = pattern.length;
        NonNullList<Ingredient> ingredientList = NonNullList.create();
        for (String row : pattern)
        {
            for (Character entry : row.toCharArray())
            {
                Optional<Tuple<Character, ? extends Ingredient>> r = Arrays.stream(ingredients).filter(t -> t.getFirst().equals(entry)).findFirst();
                if(r.isPresent())
                    ingredientList.add(r.get().getSecond());
                else
                    ingredientList.add(Ingredient.field_193370_a);
            }
        }

        CraftingManager.func_193372_a(recipeName, new ShapedRecipes("", w, h, ingredientList, output));
    }
}
