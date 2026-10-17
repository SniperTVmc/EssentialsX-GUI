package fr.snipertvmc.essentialsxgui.v2_22_0.utilities;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.utils.VersionUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.*;

/**
 * Utilities class for condensing items in Minecraft using the Essentials plugin.
 * <p>
 * This class is based on the EssentialsX source code and provides methods to condense items in a player's inventory.
 * <p>
 * Original source: {@link com.earth2me.essentials.commands.Commandcondense}
 */
public class CondenseUtils {


	// -------------------------------------------------- //


	private final IEssentials ess;
	private final Map<ItemStack, SimpleRecipe> condenseList = new HashMap<>();


	public CondenseUtils(final IEssentials ess) {
		this.ess = ess;
	}


	// -------------------------------------------------- //


	public boolean condenseItems(final User user, final List<ItemStack> items, final boolean validateReverse) throws Exception {
		if (items == null || items.isEmpty()) {
			user.sendTl("itemsNotConverted");
			throw new NoChargeException();
		}

		boolean didConvert = false;

		List<ItemStack> uniqueItems = new ArrayList<>();
		for (ItemStack stack : items) {
			if (stack == null || stack.getType() == Material.AIR) {
				continue;
			}
			boolean exists = false;
			for (ItemStack unique : uniqueItems) {
				if (unique.isSimilar(stack)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				uniqueItems.add(stack.clone());
			}
		}

		for (final ItemStack itemStack : uniqueItems) {
			if (condenseListItems(user, items, itemStack, validateReverse)) {
				didConvert = true;
			}
		}

		Iterator<ItemStack> leftoverIterator = items.iterator();
		while (leftoverIterator.hasNext()) {
			ItemStack leftover = leftoverIterator.next();
			if (leftover != null && leftover.getType() != Material.AIR) {
				HashMap<Integer, ItemStack> excess = user.getBase().getInventory().addItem(leftover);
				if (!excess.isEmpty()) {
					for (ItemStack drop : excess.values()) {
						user.getBase().getWorld().dropItemNaturally(user.getBase().getLocation(), drop);
					}
				}
			}
			leftoverIterator.remove();
		}

		user.getBase().updateInventory();

		if (didConvert) {
			user.sendTl("itemsConverted");
			return true;
		} else {
			user.sendTl("itemsNotConverted");
			throw new NoChargeException();
		}
	}


	private boolean condenseListItems(final User user, final List<ItemStack> inventoryList, final ItemStack stack, final boolean validateReverse) {
		final SimpleRecipe condenseType = getCondenseType(stack);
		if (condenseType != null) {
			final ItemStack input = condenseType.getInput();
			final ItemStack result = condenseType.getResult();

			if (validateReverse) {
				boolean pass = false;
				for (final Recipe revRecipe : ess.getServer().getRecipesFor(input)) {
					if (getStackOnRecipeMatch(revRecipe, result) != null) {
						pass = true;
						break;
					}
				}
				if (!pass) {
					return false;
				}
			}

			int amount = 0;
			for (final ItemStack contents : inventoryList) {
				if (contents != null && contents.isSimilar(stack)) {
					amount += contents.getAmount();
				}
			}

			int inputPerRecipe = input.getAmount();
			if (amount < inputPerRecipe) {
				return false;
			}

			final int numberOfRecipes = amount / inputPerRecipe;
			final int removeAmount = numberOfRecipes * inputPerRecipe;
			final int outputAmount = numberOfRecipes * result.getAmount();

			int remainingToRemove = removeAmount;
			Iterator<ItemStack> iterator = inventoryList.iterator();
			while (iterator.hasNext() && remainingToRemove > 0) {
				ItemStack current = iterator.next();
				if (current != null && current.isSimilar(stack)) {
					int currentAmount = current.getAmount();
					if (currentAmount <= remainingToRemove) {
						remainingToRemove -= currentAmount;
						iterator.remove();
					} else {
						current.setAmount(currentAmount - remainingToRemove);
						remainingToRemove = 0;
					}
				}
			}

			int remainingOutput = outputAmount;
			int maxStackSize = result.getMaxStackSize();

			while (remainingOutput > 0) {
				int add = Math.min(maxStackSize, remainingOutput);
				ItemStack payStack = result.clone();
				payStack.setAmount(add);

				HashMap<Integer, ItemStack> excess = user.getBase().getInventory().addItem(payStack);
				if (!excess.isEmpty()) {
					for (ItemStack drop : excess.values()) {
						user.getBase().getWorld().dropItemNaturally(user.getBase().getLocation(), drop);
					}
				}

				remainingOutput -= add;
			}

			return true;
		}
		return false;
	}


	private SimpleRecipe getCondenseType(final ItemStack stack) {
		if (condenseList.containsKey(stack)) {
			return condenseList.get(stack);
		}

		final Iterator<Recipe> intr = ess.getServer().recipeIterator();
		final List<SimpleRecipe> bestRecipes = new ArrayList<>();
		while (intr.hasNext()) {
			final Recipe recipe = intr.next();
			final Collection<ItemStack> recipeItems = getStackOnRecipeMatch(recipe, stack);

			if (recipeItems != null && (recipeItems.size() == 4 || recipeItems.size() == 9) && (recipeItems.size() > recipe.getResult().getAmount())) {
				final ItemStack input = stack.clone();
				input.setAmount(recipeItems.size());
				final SimpleRecipe newRecipe = new SimpleRecipe(recipe.getResult(), input);
				bestRecipes.add(newRecipe);
			}
		}
		if (!bestRecipes.isEmpty()) {
			if (bestRecipes.size() > 1) {
				bestRecipes.sort(SimpleRecipeComparator.INSTANCE);
			}
			final SimpleRecipe recipe = bestRecipes.get(0);
			condenseList.put(stack, recipe);
			return recipe;
		}
		condenseList.put(stack, null);
		return null;
	}


	private Collection<ItemStack> getStackOnRecipeMatch(final Recipe recipe, final ItemStack stack) {
		final Collection<ItemStack> inputList;

		if (recipe instanceof ShapedRecipe) {
			final ShapedRecipe sRecipe = (ShapedRecipe) recipe;
			if (sRecipe.getShape().length != sRecipe.getShape()[0].length()) {
				return null;
			}
			inputList = new ArrayList<>(sRecipe.getIngredientMap().values());
		} else if (recipe instanceof ShapelessRecipe) {
			final ShapelessRecipe slRecipe = (ShapelessRecipe) recipe;
			inputList = new ArrayList<>(slRecipe.getIngredientList());
		} else {
			return null;
		}

		boolean match = true;
		final Iterator<ItemStack> iter = inputList.iterator();
		while (iter.hasNext()) {
			final ItemStack inputSlot = iter.next();
			if (inputSlot == null) {
				iter.remove();
				continue;
			}
			if (VersionUtil.PRE_FLATTENING && inputSlot.getDurability() == Short.MAX_VALUE) {
				inputSlot.setDurability((short) 0);
			}
			if (!inputSlot.isSimilar(stack)) {
				match = false;
			}
		}

		if (match) {
			return inputList;
		}
		return null;
	}


	private static final class SimpleRecipe implements Recipe {
		private final ItemStack result;
		private final ItemStack input;

		private SimpleRecipe(final ItemStack result, final ItemStack input) {
			this.result = result;
			this.input = input;
		}

		@Override
		public ItemStack getResult() {
			return result.clone();
		}

		public ItemStack getInput() {
			return input.clone();
		}
	}


	private static class SimpleRecipeComparator implements Comparator<SimpleRecipe> {
		private static final SimpleRecipeComparator INSTANCE = new SimpleRecipeComparator();

		@Override
		public int compare(final SimpleRecipe o1, final SimpleRecipe o2) {
			return Integer.compare(o2.getInput().getAmount(), o1.getInput().getAmount());
		}
	}


	// -------------------------------------------------- //
}