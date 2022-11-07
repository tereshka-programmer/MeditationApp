package com.example.tranyapp.model.meditations

import com.example.tranyapp.model.meditations.entities.Meditation
import com.github.javafaker.Faker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class InMemoryMeditationsRepository : MeditationsRepository {

    private val currentMeditationFlow = MutableStateFlow<List<Meditation>>(listOf())

    init {
        val faker = Faker.instance()
        currentMeditationFlow.value = (0..100).map {
            Meditation(
                faker.pokemon().name(),
                5,
                value = meditationsMusic[it % meditationsMusic.size],
                image = images[it % images.size]
            )
        }
    }

    override fun getAllMeditations(): Flow<List<Meditation>> = currentMeditationFlow

    override suspend fun getMeditation(title: String): Meditation = withContext(Dispatchers.IO) {
        return@withContext currentMeditationFlow.value.first { it.title == title }
    }

    companion object {
        private val images = listOf(
            "https://images.unsplash.com/photo-1604537529428-15bcbeecfe4d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1469&q=80",
            "https://images.unsplash.com/photo-1647891940243-77a6483a152e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1445262102387-5fbb30a5e59d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1499002238440-d264edd596ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80",
            "https://images.unsplash.com/photo-1621849400072-f554417f7051?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1507041957456-9c397ce39c97?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1553114836-026cecec9778?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1501436513145-30f24e19fcc8?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=688&q=80",
            "https://images.unsplash.com/photo-1507608616759-54f48f0af0ee?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1496256654245-8f3d0ef3bebe?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1519356162333-4d49ceade668?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80",
            "https://images.unsplash.com/photo-1540979388789-6cee28a1cdc9?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1535463731090-e34f4b5098c5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80",
            "https://images.unsplash.com/photo-1504870712357-65ea720d6078?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80",
            "https://images.unsplash.com/photo-1446034295857-c39f8844fad4?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80",
            "https://images.unsplash.com/photo-1510525009512-ad7fc13eefab?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1519378058457-4c29a0a2efac?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=704&q=80",
            "https://images.unsplash.com/photo-1551376347-075b0121a65b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1530908295418-a12e326966ba?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1444930694458-01babf71870c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1563&q=80",
            "https://images.unsplash.com/photo-1542272201-b1ca555f8505?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
        )

        private val meditationsMusic = listOf(
            "https://cdn.pixabay.com/audio/2021/11/11/audio_6aa9f66065.mp3",
            "https://cdn.pixabay.com/audio/2021/10/25/audio_05570f2464.mp3",
            "https://cdn.pixabay.com/audio/2022/10/05/audio_1c7fba0237.mp3",
            "https://cdn.pixabay.com/audio/2022/05/27/audio_1808fbf07a.mp3",
            "https://cdn.pixabay.com/audio/2022/02/11/audio_5ce885b8c5.mp3"
//            "https://cdn.mp3xa.me/cPaEMk_xdSGMLAxirU0yxg/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9QYXJhZGlzZSBDaGlsbG91dCAtIENsb3VkaW5nLm1wMw",
//            "https://cdn.mp3xa.me/iV8NOeO78AWnrClRDi2v8g/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9CaW9jdW8gLSBDb25jZW50cmF0aW9uIChPcmlnaW5hbCBNaXgpLm1wMw",
//            "https://cdn.mp3xa.me/NDIcUZfvijRu8UvVIeIKFQ/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9OYW1hc3RlIE11ZHJhIC0gT3BlbiBTZW5zZXMgKE9yaWdpbmFsIE1peCkubXAz",
//            "https://cdn.mp3xa.me/sh2EW04Y2Z7bxJ0vAb6IEg/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9BZW9saWFoIC0gRm9yIFlvdXIgTG92ZSBPbmx5Lm1wMw",
//            "https://cdn.mp3xa.me/e5yOhLcWljl4N8yrTIXvYw/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9Nb3JwaGVvIC0gVW5kZXIgdGhlIFRyZWVzLm1wMw",
//            "https://cdn.mp3xa.me/BIwL5-56aRL2ZS77rJ6T8Q/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9GdWNjYSBNdXNpYyBTZW5zZSAtIERlZXAgRGV2b3Rpb24gKE9yaWdpbmFsIE1peCkubXAz",
//            "https://cdn.mp3xa.me/For3cmcqKAA9jJwvd2pP7g/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9DaW5lbWF0aWMgTWVkaXRhdGlvbiAtIEVub3VnaCBGb3IgSGFwcGluZXNzIChPcmlnaW5hbCBNaXgpLm1wMw",
//            "https://cdn.mp3xa.me/_itk2ODrXfKcGOzqKbKf1w/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9Bd2hpbmEgLSBJIFByYXllZCBGb3IgQSBNaXJhY2xlLm1wMw",
//            "https://cdn.mp3xa.me/hluQqzdBGXlgZeUzV12BhA/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9CdXZhbmEgLSBJc2h2YXJhIExpbGEgWWl0ZWxsYW0ubXAz",
//            "https://cdn.mp3xa.me/Z6URnU0_jlHaK7DrJeNbDg/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9GdW5rYW5hIC0gU29mdG5lc3MgUmVkIChaZW4gQW1iaWVudCBNZWRpdGF0aW9uKS5tcDM",
//            "https://cdn.mp3xa.me/vXSZ4tMKDJvN_2BFI0rX-w/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9SZXNzb25uaW1vIC0gU29sIE5hY2llbnRlIChZb2dhICYgTWVkaXRhdGlvbiBWZXJzaW9uKS5tcDM",
//            "https://cdn.mp3xa.me/LT-KoX27U1I94jxoL2SVMg/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9NYXRlcmlhbCBOYXR1cmUgLSBPdXQgU2lkZSAoT3JpZ2luYWwgTWl4KS5tcDM",
//            "https://cdn.mp3xa.me/-5NMYpRGIsT6hKyyNqiExg/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9OdWJibGUgTWluZCAtIFNtYWxsIFBpZWNlcyAoWW9nYSAmIE1lZGl0YXRpb24gVmVyc2lvbikubXAz",
//            "https://cdn.mp3xa.me/BbmZ3Ud7lnToJ1v6bpzsLw/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9Db2xvdXIgWW91ciBMaWZlIC0gR2hvb25nYXQgT2hsZSBOYSBMdWsgU2FqbmEubXAz",
//            "https://cdn.mp3xa.me/YskxERNFpyuMCybk9mawbg/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9EYXZpZCBTdW4gLSBZb2dhIFJlbGF4YXRpb24gMi5tcDM",
//            "https://cdn.mp3xa.me/Ico2cdsDyco7nVHTV8IkWg/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9IYXRoYSBBc2FuYSAtIFBhc2FqZSAoT3JpZ2luYWwgTWl4KS5tcDM",
//            "https://cdn.mp3xa.me/zK2pO--Bb0cRT1j9Ov7hTQ/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9TdGVwcyBJbiBUaW1lIC0gTGlvbmVzcy5tcDM",
//            "https://cdn.mp3xa.me/APyCgl2K1YXppz2yM5ivbw/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9Zb2dhIEdvYSAtIFdhdmUgRmxvdyAoT3JpZ2luYWwgTWl4KS5tcDM",
//            "https://cdn.mp3xa.me/YtZRZRXBxbo9BXTVkrfHFQ/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy84czggLSBDb3NtaWNhIChPcmlnaW5hbCBNaXgpLm1wMw",
//            "https://cdn.mp3xa.me/mIfFsGwmCYUzZQklzpk7_Q/1667785600/L29ubGluZS9tcDMvMjAyMC8wMy9Tb290aGluZyBUb3VjaCAtIERlc2VydCBGbG93ZXIubXAz"
        )
    }

}