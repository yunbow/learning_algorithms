// Kotlin
// データ構造: マップ (Map)

class MapData {
    private val _data = mutableMapOf<String, Int>()

    fun get(): List<Pair<String, Int>> = _data.toList()
    
    fun getKeys(): List<String> = _data.keys.toList()
    
    fun getValues(): List<Int> = _data.values.toList()
    
    fun getKey(value: Int): String? {
        return _data.entries.find { it.value == value }?.key.also {
            if (it == null) println("ERROR: $value は範囲外です")
        }
    }
    
    fun getValue(key: String): Int? {
        return _data[key].also {
            if (it == null) println("ERROR: $key は範囲外です")
        }
    }

    fun add(key: String, value: Int): Boolean {
        return if (key in _data) {
            println("ERROR: $key は重複です")
            false
        } else {
            _data[key] = value
            true
        }
    }
    
    fun remove(key: String): Boolean {
        return if (key in _data) {
            _data.remove(key)
            true
        } else {
            println("ERROR: $key は範囲外です")
            false
        }
    }
    
    fun update(key: String, value: Int): Boolean {
        return if (key in _data) {
            _data[key] = value
            true
        } else {
            println("ERROR: $key は範囲外です")
            false
        }
    }
    
    fun isEmpty(): Boolean = _data.isEmpty()
    
    fun size(): Int = _data.size
    
    fun clear(): Boolean {
        _data.clear()
        return true
    }
}

fun main() {
    println("Map TEST -----> start")

    println("\nnew")
    val mapData = MapData()
    println("  現在のデータ: ${mapData.get()}")

    println("\nis_empty")
    val outputIsEmpty = mapData.isEmpty()
    println("  出力値: $outputIsEmpty")

    println("\nsize")
    val outputSize = mapData.size()
    println("  出力値: $outputSize")

    println("\nadd")
    val inputAdd1 = Pair("apple", 100)
    println("  入力値: $inputAdd1")
    val outputAdd1 = mapData.add(inputAdd1.first, inputAdd1.second)
    println("  出力値: $outputAdd1")
    println("  現在のデータ: ${mapData.get()}")

    println("\nadd")
    val inputAdd2 = Pair("banana", 150)
    println("  入力値: $inputAdd2")
    val outputAdd2 = mapData.add(inputAdd2.first, inputAdd2.second)
    println("  出力値: $outputAdd2")
    println("  現在のデータ: ${mapData.get()}")

    println("\nadd")
    val inputAdd3 = Pair("apple", 200)
    println("  入力値: $inputAdd3")
    val outputAdd3 = mapData.add(inputAdd3.first, inputAdd3.second)
    println("  出力値: $outputAdd3")
    println("  現在のデータ: ${mapData.get()}")

    println("\nsize")
    val outputSize2 = mapData.size()
    println("  出力値: $outputSize2")

    println("\nget")
    val inputGet1 = "apple"
    println("  入力値: $inputGet1")
    val outputGet1 = mapData.getValue(inputGet1)
    println("  出力値: $outputGet1")

    println("\nget")
    val inputGet2 = "orange"
    println("  入力値: $inputGet2")
    val outputGet2 = mapData.getValue(inputGet2)
    println("  出力値: $outputGet2")

    println("\nupdate")
    val inputUpdate1 = Pair("banana", 180)
    println("  入力値: $inputUpdate1")
    val outputUpdate1 = mapData.update(inputUpdate1.first, inputUpdate1.second)
    println("  出力値: $outputUpdate1")
    println("  現在のデータ: ${mapData.get()}")

    println("\nupdate")
    val inputUpdate2 = Pair("orange", 250)
    println("  入力値: $inputUpdate2")
    val outputUpdate2 = mapData.update(inputUpdate2.first, inputUpdate2.second)
    println("  出力値: $outputUpdate2")
    println("  現在のデータ: ${mapData.get()}")

    println("\nget")
    val inputGet3 = "banana"
    val outputGet3 = mapData.getValue(inputGet3)
    println("  出力値: $outputGet3")

    println("\nget_keys")
    val outputGetKeys = mapData.getKeys()
    println("  出力値: $outputGetKeys")

    println("\nvalues")
    val outputGetValues = mapData.getValues()
    println("  出力値: $outputGetValues")

    println("\nget_key")
    val inputGetKey1 = 180
    println("  入力値: $inputGetKey1")
    val outputGetKey1 = mapData.getKey(inputGetKey1)
    println("  出力値: $outputGetKey1")

    println("\nget_key")
    val inputGetKey2 = 500
    println("  入力値: $inputGetKey2")
    val outputGetKey2 = mapData.getKey(inputGetKey2)
    println("  出力値: $outputGetKey2")

    println("\nremove")
    val inputRemove1 = "apple"
    println("  入力値: $inputRemove1")
    val outputRemove1 = mapData.remove(inputRemove1)
    println("  出力値: $outputRemove1")
    println("  現在のデータ: ${mapData.get()}")

    println("\nremove")
    val inputRemove2 = "orange"
    println("  入力値: $inputRemove2")
    val outputRemove2 = mapData.remove(inputRemove2)
    println("  出力値: $outputRemove2")
    println("  現在のデータ: ${mapData.get()}")

    println("\nsize")
    val outputSize3 = mapData.size()
    println("  出力値: $outputSize3")

    println("\nget_keys")
    val outputGetKeys2 = mapData.getKeys()
    println("  出力値: $outputGetKeys2")

    println("\nclear")
    val outputClear = mapData.clear()
    println("  出力値: $outputClear")
    println("  現在のデータ: ${mapData.get()}")

    println("\nsize")
    val outputSize4 = mapData.size()
    println("  出力値: $outputSize4")

    println("\nis_empty")
    val outputIsEmpty2 = mapData.isEmpty()
    println("  出力値: $outputIsEmpty2")

    println("\nMap TEST <----- end")
}
