package net.xalcon.torchmaster.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;

class EntityInfoWrapper
{
    private ResourceLocation entityName;
    private EntityType<?> entityType;

    EntityInfoWrapper(ResourceLocation entityName, EntityType<?> entityType)
    {
        this.entityName = entityName;
        this.entityType = entityType;
    }

    ResourceLocation getEntityName()
    {
        return entityName;
    }

    EntityType<?> getEntityType()
    {
        return entityType;
    }
}
