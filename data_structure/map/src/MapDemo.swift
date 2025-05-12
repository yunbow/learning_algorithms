// Swift
// データ構造: マップ (Map)

import Foundation

class MapData {
    private var data: [String: Int] = [:]
    
    func get() -> [(String, Int)] {
        return Array(data)
    }
    
    func getKeys() -> [String] {
        return Array(data.keys)
    }
    
    func getValues() -> [Int] {
        return Array(data.values)
    }
    
    func getKey(value: Int) -> String? {
        return data.first { $0.value == value }?.key
    }
    
    func getValue(key: String) -> Int? {
        return data[key]
    }
    
    @discardableResult
    func add(key: String, value: Int) -> Bool {
        guard data[key] == nil else {
            print("ERROR: \(key) は重複です")
            return false
        }
        data[key] = value
        return true
    }
    
    @discardableResult
    func remove(key: String) -> Bool {
        guard data[key] != nil else {
            print("ERROR: \(key) は範囲外です")
            return false
        }
        data.removeValue(forKey: key)
        return true
    }
    
    @discardableResult
    func update(key: String, value: Int) -> Bool {
        guard data[key] != nil else {
            print("ERROR: \(key) は範囲外です")
            return false
        }
        data[key] = value
        return true
    }
    
    func isEmpty() -> Bool {
        return data.isEmpty
    }
    
    func size() -> Int {
        return data.count
    }
    
    @discardableResult
    func clear() -> Bool {
        data.removeAll()
        return true
    }
}

func main() {
    print("Map TEST -----> start")
    
    print("\nnew")
    let mapData = MapData()
    print("  現在のデータ: \(mapData.get())")
    
    print("\nis_empty")
    let outputEmpty = mapData.isEmpty()
    print("  出力値: \(outputEmpty)")
    
    print("\nsize")
    let outputSize = mapData.size()
    print("  出力値: \(outputSize)")
    
    print("\nadd")
    let input1 = ("apple", 100)
    print("  入力値: \(input1)")
    let output1 = mapData.add(key: input1.0, value: input1.1)
    print("  出力値: \(output1)")
    print("  現在のデータ: \(mapData.get())")
    
    print("\nadd")
    let input2 = ("banana", 150)
    print("  入力値: \(input2)")
    let output2 = mapData.add(key: input2.0, value: input2.1)
    print("  出力値: \(output2)")
    print("  現在のデータ: \(mapData.get())")
    
    print("\nadd")
    let input3 = ("apple", 200)
    print("  入力値: \(input3)")
    let output3 = mapData.add(key: input3.0, value: input3.1)
    print("  出力値: \(output3)")
    print("  現在のデータ: \(mapData.get())")
    
    print("\nsize")
    let outputSize2 = mapData.size()
    print("  出力値: \(outputSize2)")
    
    print("\nget")
    let input4 = "apple"
    print("  入力値: \(input4)")
    let output4 = mapData.getValue(key: input4)
    print("  出力値: \(output4 ?? "nil")")
    
    print("\nget")
    let input5 = "orange"
    print("  入力値: \(input5)")
    let output5 = mapData.getValue(key: input5)
    print("  出力値: \(output5 ?? "nil")")
    
    print("\nupdate")
    let input6 = ("banana", 180)
    print("  入力値: \(input6)")
    let output6 = mapData.update(key: input6.0, value: input6.1)
    print("  出力値: \(output6)")
    print("  現在のデータ: \(mapData.get())")
    
    print("\nupdate")
    let input7 = ("orange", 250)
    print("  入力値: \(input7)")
    let output7 = mapData.update(key: input7.0, value: input7.1)
    print("  出力値: \(output7)")
    print("  現在のデータ: \(mapData.get())")
    
    print("\nget")
    let input8 = "banana"
    let output8 = mapData.getValue(key: input8)
    print("  出力値: \(output8 ?? "nil")")
    
    print("\nget_keys")
    let outputKeys = mapData.getKeys()
    print("  出力値: \(outputKeys)")
    
    print("\nvalues")
    let outputValues = mapData.getValues()
    print("  出力値: \(outputValues)")
    
    print("\nget_key")
    let input9 = 180
    print("  入力値: \(input9)")
    let output9 = mapData.getKey(value: input9)
    print("  出力値: \(output9 ?? "nil")")
    
    print("\nget_key")
    let input10 = 500
    print("  入力値: \(input10)")
    let output10 = mapData.getKey(value: input10)
    print("  出力値: \(output10 ?? "nil")")
    
    print("\nremove")
    let input11 = "apple"
    print("  入力値: \(input11)")
    let output11 = mapData.remove(key: input11)
    print("  出力値: \(output11)")
    print("  現在のデータ: \(mapData.get())")
    
    print("\nremove")
    let input12 = "orange"
    print("  入力値: \(input12)")
    let output12 = mapData.remove(key: input12)
    print("  出力値: \(output12)")
    print("  現在のデータ: \(mapData.get())")
    
    print("\nsize")
    let outputSize3 = mapData.size()
    print("  出力値: \(outputSize3)")
    
    print("\nget_keys")
    let outputKeys2 = mapData.getKeys()
    print("  出力値: \(outputKeys2)")
    
    print("\nclear")
    let outputClear = mapData.clear()
    print("  出力値: \(outputClear)")
    print("  現在のデータ: \(mapData.get())")
    
    print("\nsize")
    let outputSize4 = mapData.size()
    print("  出力値: \(outputSize4)")
    
    print("\nis_empty")
    let outputEmpty2 = mapData.isEmpty()
    print("  出力値: \(outputEmpty2)")
    
    print("\nMap TEST <----- end")
}

main()