package net.xalcon.torchmaster.common.utils;

import net.minecraft.nbt.NBTTagList;

import java.util.Objects;

public class NbtUtils
{
	public static boolean isStringInTagList(String value, NBTTagList tagList)
	{
		for(int i = 0; i < tagList.tagCount(); i++)
			if(Objects.equals(tagList.getStringTagAt(i), value))
				return true;
		return false;
	}
}
