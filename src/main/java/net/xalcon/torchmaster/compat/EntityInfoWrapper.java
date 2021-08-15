package net.xalcon.torchmaster.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

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
