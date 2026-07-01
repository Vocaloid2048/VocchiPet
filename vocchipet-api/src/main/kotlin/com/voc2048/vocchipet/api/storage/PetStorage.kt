package com.voc2048.vocchipet.api.storage

import com.voc2048.vocchipet.api.Pet
import java.util.UUID
import java.util.concurrent.CompletableFuture

/**
 * 定義寵物數據儲存與讀取的介面。
 * Defines the interface for pet data storage and retrieval.
 */
interface PetStorage {

    /**
     * 初始化儲存媒介，例如建立資料表。
     * Initializes the storage medium, e.g., creating tables.
     *
     * @return 非同步執行的 CompletableFuture / A CompletableFuture for asynchronous execution.
     */
    fun init(): CompletableFuture<Void>

    /**
     * 非同步儲存寵物數據。
     * Asynchronously saves pet data.
     *
     * @param pet 要儲存的寵物實例 / The pet instance to save.
     * @return 非同步執行的 CompletableFuture / A CompletableFuture for asynchronous execution.
     */
    fun savePet(pet: Pet): CompletableFuture<Void>

    /**
     * 非同步讀取特定 UUID 的寵物數據。
     * Asynchronously loads pet data for a specific UUID.
     *
     * @param petId 寵物的唯一識別碼 / The unique identifier of the pet.
     * @return 包含寵物實例的 CompletableFuture，若不存在則為 null / A CompletableFuture containing the pet instance, or null if not found.
     */
    fun loadPet(petId: UUID): CompletableFuture<Pet?>

    /**
     * 非同步讀取特定玩家擁有的所有寵物。
     * Asynchronously loads all pets owned by a specific player.
     *
     * @param ownerId 玩家的唯一識別碼 / The unique identifier of the owner.
     * @return 包含寵物清單的 CompletableFuture / A CompletableFuture containing a list of pets.
     */
    fun loadPetsByOwner(ownerId: UUID): CompletableFuture<List<Pet>>
}
