package com.teamxenox.codoc19.core.geography

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.teamxenox.bootzan.GsonUtils
import com.teamxenox.codoc19.models.Country
import java.io.File


data class CountryItem(
        @SerializedName("altSpellings")
        val altSpellings: List<String>,
        @SerializedName("cca2")
        val cca2: String, // ZW
        @SerializedName("cca3")
        val cca3: String, // ZWE
        @SerializedName("ccn3")
        val ccn3: String, // 716
        @SerializedName("cioc")
        val cioc: String, // ZIM
        @SerializedName("name")
        val name: Name,
        @SerializedName("region")
        val region: String, // Africa
        @SerializedName("status")
        val status: String, // officially-assigned
        @SerializedName("subregion")
        val subregion: String, // Eastern Africa
        @SerializedName("tld")
        val tld: List<String>,
        @SerializedName("translations")
        val translations: Translations
) {
    data class Currencies(
            @SerializedName("BWP")
            val bWP: BWP,
            @SerializedName("CNY")
            val cNY: CNY,
            @SerializedName("EUR")
            val eUR: EUR,
            @SerializedName("GBP")
            val gBP: GBP,
            @SerializedName("INR")
            val iNR: INR,
            @SerializedName("JPY")
            val jPY: JPY,
            @SerializedName("USD")
            val uSD: USD,
            @SerializedName("ZAR")
            val zAR: ZAR,
            @SerializedName("ZWB")
            val zWB: ZWB
    ) {
        data class BWP(
                @SerializedName("name")
                val name: String, // Botswana pula
                @SerializedName("symbol")
                val symbol: String // P
        )

        data class CNY(
                @SerializedName("name")
                val name: String, // Chinese yuan
                @SerializedName("symbol")
                val symbol: String // ¥
        )

        data class EUR(
                @SerializedName("name")
                val name: String, // Euro
                @SerializedName("symbol")
                val symbol: String // €
        )

        data class GBP(
                @SerializedName("name")
                val name: String, // British pound
                @SerializedName("symbol")
                val symbol: String // £
        )

        data class INR(
                @SerializedName("name")
                val name: String, // Indian rupee
                @SerializedName("symbol")
                val symbol: String // ₹
        )

        data class JPY(
                @SerializedName("name")
                val name: String, // Japanese yen
                @SerializedName("symbol")
                val symbol: String // ¥
        )

        data class USD(
                @SerializedName("name")
                val name: String, // United States dollar
                @SerializedName("symbol")
                val symbol: String // $
        )

        data class ZAR(
                @SerializedName("name")
                val name: String, // South African rand
                @SerializedName("symbol")
                val symbol: String // Rs
        )

        data class ZWB(
                @SerializedName("name")
                val name: String, // Zimbabwean bonds
                @SerializedName("symbol")
                val symbol: String // $
        )
    }

    data class Demonyms(
            @SerializedName("eng")
            val eng: Eng,
            @SerializedName("fra")
            val fra: Fra
    ) {
        data class Eng(
                @SerializedName("f")
                val f: String, // Zimbabwean
                @SerializedName("m")
                val m: String // Zimbabwean
        )

        data class Fra(
                @SerializedName("f")
                val f: String, // Zimbabwéenne
                @SerializedName("m")
                val m: String // Zimbabwéen
        )
    }

    data class Idd(
            @SerializedName("root")
            val root: String, // +2
            @SerializedName("suffixes")
            val suffixes: List<String>
    )

    data class Languages(
            @SerializedName("bwg")
            val bwg: String, // Chibarwe
            @SerializedName("eng")
            val eng: String, // English
            @SerializedName("kck")
            val kck: String, // Kalanga
            @SerializedName("khi")
            val khi: String, // Khoisan
            @SerializedName("ndc")
            val ndc: String, // Ndau
            @SerializedName("nde")
            val nde: String, // Northern Ndebele
            @SerializedName("nya")
            val nya: String, // Chewa
            @SerializedName("sna")
            val sna: String, // Shona
            @SerializedName("sot")
            val sot: String, // Sotho
            @SerializedName("toi")
            val toi: String, // Tonga
            @SerializedName("tsn")
            val tsn: String, // Tswana
            @SerializedName("tso")
            val tso: String, // Tsonga
            @SerializedName("ven")
            val ven: String, // Venda
            @SerializedName("xho")
            val xho: String, // Xhosa
            @SerializedName("zib")
            val zib: String // Zimbabwean Sign Language
    )

    data class Name(
            @SerializedName("common")
            val common: String, // Zimbabwe
            @SerializedName("native")
            val native: Native,
            @SerializedName("official")
            val official: String // Republic of Zimbabwe
    ) {
        data class Native(
                @SerializedName("bwg")
                val bwg: Bwg,
                @SerializedName("eng")
                val eng: Eng,
                @SerializedName("kck")
                val kck: Kck,
                @SerializedName("khi")
                val khi: Khi,
                @SerializedName("ndc")
                val ndc: Ndc,
                @SerializedName("nde")
                val nde: Nde,
                @SerializedName("nya")
                val nya: Nya,
                @SerializedName("sna")
                val sna: Sna,
                @SerializedName("sot")
                val sot: Sot,
                @SerializedName("toi")
                val toi: Toi,
                @SerializedName("tsn")
                val tsn: Tsn,
                @SerializedName("tso")
                val tso: Tso,
                @SerializedName("ven")
                val ven: Ven,
                @SerializedName("xho")
                val xho: Xho,
                @SerializedName("zib")
                val zib: Zib
        ) {
            data class Bwg(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Eng(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Kck(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Khi(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Ndc(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Nde(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Nya(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Sna(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Sot(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Toi(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Tsn(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Tso(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Ven(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Xho(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )

            data class Zib(
                    @SerializedName("common")
                    val common: String, // Zimbabwe
                    @SerializedName("official")
                    val official: String // Republic of Zimbabwe
            )
        }
    }

    data class Translations(
            @SerializedName("ces")
            val ces: Ces,
            @SerializedName("deu")
            val deu: Deu,
            @SerializedName("est")
            val est: Est,
            @SerializedName("fin")
            val fin: Fin,
            @SerializedName("fra")
            val fra: Fra,
            @SerializedName("hrv")
            val hrv: Hrv,
            @SerializedName("ita")
            val ita: Ita,
            @SerializedName("jpn")
            val jpn: Jpn,
            @SerializedName("kor")
            val kor: Kor,
            @SerializedName("nld")
            val nld: Nld,
            @SerializedName("per")
            val per: Per,
            @SerializedName("pol")
            val pol: Pol,
            @SerializedName("por")
            val por: Por,
            @SerializedName("rus")
            val rus: Rus,
            @SerializedName("slk")
            val slk: Slk,
            @SerializedName("spa")
            val spa: Spa,
            @SerializedName("urd")
            val urd: Urd,
            @SerializedName("zho")
            val zho: Zho
    ) {
        data class Ces(
                @SerializedName("common")
                val common: String, // Zimbabwe
                @SerializedName("official")
                val official: String // Zimbabwská republika
        )

        data class Deu(
                @SerializedName("common")
                val common: String, // Simbabwe
                @SerializedName("official")
                val official: String // Republik Simbabwe
        )

        data class Est(
                @SerializedName("common")
                val common: String, // Zimbabwe
                @SerializedName("official")
                val official: String // Zimbabwe Vabariik
        )

        data class Fin(
                @SerializedName("common")
                val common: String, // Zimbabwe
                @SerializedName("official")
                val official: String // Zimbabwen tasavalta
        )

        data class Fra(
                @SerializedName("common")
                val common: String, // Zimbabwe
                @SerializedName("official")
                val official: String // République du Zimbabwe
        )

        data class Hrv(
                @SerializedName("common")
                val common: String, // Zimbabve
                @SerializedName("official")
                val official: String // Republika Zimbabve
        )

        data class Ita(
                @SerializedName("common")
                val common: String, // Zimbabwe
                @SerializedName("official")
                val official: String // Repubblica dello Zimbabwe
        )

        data class Jpn(
                @SerializedName("common")
                val common: String, // ジンバブエ
                @SerializedName("official")
                val official: String // ジンバブエ共和国
        )

        data class Kor(
                @SerializedName("common")
                val common: String, // 짐바브웨
                @SerializedName("official")
                val official: String // 짐바브웨 공화국
        )

        data class Nld(
                @SerializedName("common")
                val common: String, // Zimbabwe
                @SerializedName("official")
                val official: String // Republiek Zimbabwe
        )

        data class Per(
                @SerializedName("common")
                val common: String, // زیمبابوه
                @SerializedName("official")
                val official: String // جمهوری زیمبابوه
        )

        data class Pol(
                @SerializedName("common")
                val common: String, // Zimbabwe
                @SerializedName("official")
                val official: String // Republika Zimbabwe
        )

        data class Por(
                @SerializedName("common")
                val common: String, // Zimbabwe
                @SerializedName("official")
                val official: String // República do Zimbabwe
        )

        data class Rus(
                @SerializedName("common")
                val common: String, // Зимбабве
                @SerializedName("official")
                val official: String // Республика Зимбабве
        )

        data class Slk(
                @SerializedName("common")
                val common: String, // Zimbabwe
                @SerializedName("official")
                val official: String // Zimbabwianska republika
        )

        data class Spa(
                @SerializedName("common")
                val common: String, // Zimbabue
                @SerializedName("official")
                val official: String // República de Zimbabue
        )

        data class Urd(
                @SerializedName("common")
                val common: String, // زمبابوے
                @SerializedName("official")
                val official: String // جمہوریہ زمبابوے
        )

        data class Zho(
                @SerializedName("common")
                val common: String, // 津巴布韦
                @SerializedName("official")
                val official: String // 津巴布韦共和国
        )
    }
}

fun main() {
    val type = object : TypeToken<List<CountryItem>>() {}.type
    val fullCountriesJson = File("assets/countries_full.json").readText()
    val fCountries = GsonUtils.gson.fromJson<List<CountryItem>>(fullCountriesJson, type)
    val countries = mutableListOf<Country>()
    for (fCountry in fCountries) {
        countries.add(Country(
                fCountry.cca2,
                fCountry.name.common,
                setOf(
                        fCountry.name.official,
                        fCountry.ccn3,
                        fCountry.cca3,
                        fCountry.cioc,
                        *fCountry.altSpellings.toSet().toTypedArray()
                )
        ))
    }
    val countriesJson = GsonUtils.gson.toJson(countries)
    File("assets/countries.json").writeText(countriesJson)
}