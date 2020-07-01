package com.google.ar.sceneform.rendering

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.future.await

/** Utility class used to construct default {@link Material}s. */
@RequiresApi(api = Build.VERSION_CODES.N)
object MaterialFactory {

    /**
     * Name of material parameter for controlling the color of {@link #makeOpaqueWithColor(Context,
     * Color)} and {@link #makeTransparentWithColor(Context, Color)} materials.
     *
     * @see Material#setFloat3(String, Color)
     * @see Material#setFloat4(String, Color)
     */
    const val MATERIAL_COLOR = "color"

    /**
     * Name of material parameter for controlling the texture of {@link
     * #makeOpaqueWithTexture(Context, Texture)} and {@link #makeTransparentWithTexture(Context,
     * Texture)} materials.
     *
     * @see Material#setTexture(String, Texture)
     */
    const val MATERIAL_TEXTURE = "texture"

    /**
     * Name of material parameter for controlling the metallic property of all {@link MaterialFactory}
     * materials. The metallic property defines whether the surface is a metallic (conductor) or a
     * non-metallic (dielectric) surface. This property should be used as a binary value, set to
     * either 0 or 1. Intermediate values are only truly useful to create transitions between
     * different types of surfaces when using textures. The default value is 0.
     *
     * @see Material#setFloat(String, float)
     */
    const val MATERIAL_METALLIC = "metallic"

    /**
     * Name of material parameter for controlling the roughness property of all {@link
     * MaterialFactory} materials. The roughness property controls the perceived smoothness of the
     * surface. When roughness is set to 0, the surface is perfectly smooth and highly glossy. The
     * rougher a surface is, the “blurrier” the reflections are. The default value is 0.4.
     *
     * @see Material#setFloat(String, float)
     */
    const val MATERIAL_ROUGHNESS = "roughness"

    /**
     * Name of material parameter for controlling the reflectance property of all {@link
     * MaterialFactory} materials. The reflectance property only affects non-metallic surfaces. This
     * property can be used to control the specular intensity. This value is defined between 0 and 1
     * and represents a remapping of a percentage of reflectance. The default value is 0.5.
     *
     * @see Material#setFloat(String, float)
     */
    const val MATERIAL_REFLECTANCE = "reflectance"

    private const val DEFAULT_METALLIC_PROPERTY = 0.0f
    private const val DEFAULT_ROUGHNESS_PROPERTY = 0.4f
    private const val DEFAULT_REFLECTANCE_PROPERTY = 0.5f

    /**
     * Creates an opaque {@link Material} with the {@link Color} passed in. The {@link Color} can be
     * modified by calling {@link Material#setFloat3(String, Color)} with {@link #MATERIAL_COLOR}. The
     * metallicness, roughness, and reflectance can be modified using {@link Material#setFloat(String,
     * float)}.
     *
     * @see #MATERIAL_METALLIC
     * @see #MATERIAL_ROUGHNESS
     * @see #MATERIAL_REFLECTANCE
     * @param context a context used for loading the material resource
     * @param color the color for the material to render
     * @return material that will render the given color
     */
    suspend fun makeOpaqueWithColor(context: Context, color: Color): Material =
        Material.builder()
            .setSource(
                context,
                RenderingResources.GetSceneformResource(
                    context, RenderingResources.Resource.OPAQUE_COLORED_MATERIAL))
            .build()
            .await().apply {
                setFloat3(MATERIAL_COLOR, color)
                applyDefaultPbrParams()
            }

    /**
     * Creates a transparent {@link Material} with the {@link Color} passed in. The {@link Color} can
     * be modified by calling {@link Material#setFloat4(String, Color)} with {@link #MATERIAL_COLOR}.
     * The metallicness, roughness, and reflectance can be modified using {@link
     * Material#setFloat(String, float)}.
     *
     * @see #MATERIAL_METALLIC
     * @see #MATERIAL_ROUGHNESS
     * @see #MATERIAL_REFLECTANCE
     * @param context a context used for loading the material resource
     * @param color the color for the material to render
     * @return material that will render the given color
     */
    suspend fun makeTransparentWithColor(context: Context, color: Color): Material =
        Material.builder()
            .setSource(
                context,
                RenderingResources.GetSceneformResource(
                    context, RenderingResources.Resource.TRANSPARENT_COLORED_MATERIAL))
            .build()
            .await().apply {
                setFloat4(MATERIAL_COLOR, color)
                applyDefaultPbrParams()
            }

    /**
     * Creates an opaque {@link Material} with the {@link Texture} passed in. The {@link Texture} can
     * be modified by calling {@link Material#setTexture(String, Texture)} with {@link
     * #MATERIAL_TEXTURE}. The metallicness, roughness, and reflectance can be modified using {@link
     * Material#setFloat(String, float)}.
     *
     * @see #MATERIAL_METALLIC
     * @see #MATERIAL_ROUGHNESS
     * @see #MATERIAL_REFLECTANCE
     * @param context a context used for loading the material resource
     * @param texture the texture for the material to render
     * @return material that will render the given texture
     */
    suspend fun makeOpaqueWithTexture(context: Context, texture: Texture): Material =
        Material.builder()
            .setSource(
                context,
                RenderingResources.GetSceneformResource(
                    context, RenderingResources.Resource.OPAQUE_TEXTURED_MATERIAL))
            .build()
            .await().apply {
                setTexture(MATERIAL_TEXTURE, texture)
                applyDefaultPbrParams()
            }

    /**
     * Creates a transparent {@link Material} with the {@link Texture} passed in. The {@link Texture}
     * can be modified by calling {@link Material#setTexture(String, Texture)} with {@link
     * #MATERIAL_TEXTURE}. The metallicness, roughness, and reflectance can be modified using {@link
     * Material#setFloat(String, float)}.
     *
     * @see #MATERIAL_METALLIC
     * @see #MATERIAL_ROUGHNESS
     * @see #MATERIAL_REFLECTANCE
     * @param context a context used for loading the material resource
     * @param texture the texture for the material to render
     * @return material that will render the given texture
     */
    suspend fun makeTransparentWithTexture(context: Context, texture: Texture): Material =
        Material.builder()
            .setSource(
                context,
                RenderingResources.GetSceneformResource(
                    context, RenderingResources.Resource.TRANSPARENT_TEXTURED_MATERIAL))
            .build()
            .await().apply {
                setTexture(MATERIAL_TEXTURE, texture)
                applyDefaultPbrParams()
            }

    private fun Material.applyDefaultPbrParams() {
        setFloat(MATERIAL_METALLIC, DEFAULT_METALLIC_PROPERTY)
        setFloat(MATERIAL_ROUGHNESS, DEFAULT_ROUGHNESS_PROPERTY)
        setFloat(MATERIAL_REFLECTANCE, DEFAULT_REFLECTANCE_PROPERTY)
    }
}