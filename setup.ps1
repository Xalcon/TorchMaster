param(
    [Parameter(Mandatory)]
    [string]$modid = "testmod",

    [Parameter(Mandatory)]
    [string]$modName = "TestMod",

    # [Parameter(Mandatory)]
    [string]$group = "net.xalcon",

    # [Parameter(Mandatory)]
    [string]$author = "Xalcon",

    [switch]$enableForgeAccessTransformers,

    [string]$mcVersion = "1.19",
    [string]$forgeVersion = "latest",
    [string]$fabricVersion = "latest",
    [string]$fabricLoaderVersion = "latest"
)


function Replace {
    [CmdletBinding()]
    Param(
        [Parameter(ValueFromPipeline)]
        [string]$input,
        [Parameter(Mandatory, Position = 0)]
        [string]$pattern,
        [Parameter(Mandatory, Position = 1)]
        [string]$replacement
    )
    return $input -replace $pattern, $replacement
}


function Get-LatestForgeVersion {
    [CmdletBinding()]
    param(
        $mcVersion
    )
    $response = Invoke-WebRequest "https://files.minecraftforge.net/net/minecraftforge/forge/index_$mcVersion.html"
    if($response.Content -match 'class="download-version">[^\d]+(?<version>\d+\.\d+\.\d+)[^\d]+<i class="promo-latest')
    {
        return $Matches.version
    }
    Write-Error "Unable to find forge version"
}

function Get-LatestFabricVersion {
    [CmdletBinding()]
    param (
        $mcVersion
    )
    ((Invoke-WebRequest https://maven.fabricmc.net/jdlist.txt).Content.Split("`n") | where { $_ -match "^fabric-api-.*\+$mcVersion`$" } | Sort-Object -Descending | Select-Object -First 1) -replace "fabric-api-", ""
}

function Get-LatestFabricLoaderVersion {
    ((Invoke-WebRequest https://maven.fabricmc.net/jdlist.txt).Content.Split("`n") | where { $_ -match "^fabric-loader-\d+\.\d{2,}" } | Sort-Object -Descending | Select-Object -First 1) -replace "fabric-loader-", ""
}


if($forgeVersion -eq "latest")
{
    $forgeVersion = Get-LatestForgeVersion -mcVersion $mcVersion
}

if($fabricVersion -eq "latest")
{
    $fabricVersion = Get-LatestFabricVersion -mcVersion $mcVersion
}

if($fabricLoaderVersion -eq "latest")
{
    $fabricLoaderVersion = Get-LatestFabricLoaderVersion
}

## gradle.properties
Get-Content -Raw "./gradle.properties" `
    | Replace '(?:group=)([^\n]+)' "group=$group" `
    | Replace '(?:mod_name=)([^\n]+)' "mod_name=$modName" `
    | Replace '(?:mod_id=)([^\n]+)' "mod_id=$modid" `
    | Replace '(?:mod_author=)([^\n]+)' "mod_author=$author" `
    | Replace '(?:minecraft_version=)([^\n]+)' "minecraft_version=$mcVersion" `
    | Replace '(?:forge_version=)([^\n]+)' "forge_version=$forgeVersion" `
    | Replace '(//)?forge_ats_enabled=(true|false)' "forge_ats_enabled=$($enableForgeAccessTransformers.ToString().ToLower())" `
    | Replace '(?:fabric_version=)([^\n]+)' "fabric_version=$fabricVersion" `
    | Replace '(?:fabric_loader_version=)([^\n]+)' "fabric_loader_version=$fabricLoaderVersion" `
    | Set-Content -Path "./gradle.properties"

## settings.gradle
Get-Content -Raw "./settings.gradle" `
    | Replace "rootProject.name = '[^']+'" "rootProject.name = '$($modName)'" `
    | Set-Content -Path "./settings.gradle"

