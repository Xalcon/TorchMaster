package net.xalcon.torchmaster.compat;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

class EntityInfoWrapper
{
    private ResourceLocation entityName;
    private Class<? extends Entity> entityClass;

    EntityInfoWrapper(ResourceLocation entityName, Class<? extends Entity> entityClass)
    {
        this.entityName = entityName;
        this.entityClass = entityClass;
    }

    ResourceLocation getEntityName()
    {
        return entityName;
    }

    Class<? extends Entity> getEntityClass()
    {
        return entityClass;
    }
}
