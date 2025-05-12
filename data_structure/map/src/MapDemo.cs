// C#
// データ構造: マップ (Map)

using System;
using System.Collections.Generic;
using System.Linq;

class MapData
{
    private Dictionary<string, int> _data;

    public MapData()
    {
        _data = new Dictionary<string, int>();
    }

    public List<KeyValuePair<string, int>> Get()
    {
        return _data.ToList();
    }
    
    public List<string> GetKeys()
    {
        return _data.Keys.ToList();
    }
    
    public List<int> GetValues()
    {
        return _data.Values.ToList();
    }
    
    public string GetKey(int value)
    {
        try
        {
            return _data.FirstOrDefault(x => x.Value == value).Key;
        }
        catch
        {
            Console.WriteLine($"ERROR: {value} は範囲外です");
            return null;
        }
    }
    
    public int? GetValue(string key)
    {
        if (_data.TryGetValue(key, out int value))
        {
            return value;
        }
        
        Console.WriteLine($"ERROR: {key} は範囲外です");
        return null;
    }

    public bool Add(string key, int value)
    {
        if (_data.ContainsKey(key))
        {
            Console.WriteLine($"ERROR: {key} は重複です");
            return false;
        }
        
        _data[key] = value;
        return true;
    }
    
    public bool Remove(string key)
    {
        if (_data.Remove(key))
        {
            return true;
        }
        
        Console.WriteLine($"ERROR: {key} は範囲外です");
        return false;
    }
    
    public bool Update(string key, int value)
    {
        if (_data.ContainsKey(key))
        {
            _data[key] = value;
            return true;
        }
        
        Console.WriteLine($"ERROR: {key} は範囲外です");
        return false;
    }
    
    public bool IsEmpty()
    {
        return _data.Count == 0;
    }
    
    public int Size()
    {
        return _data.Count;
    }
    
    public bool Clear()
    {
        _data.Clear();
        return true;
    }

    static void Main(string[] args)
    {
        Console.WriteLine("Map TEST -----> start");

        Console.WriteLine("\nnew");
        MapData mapData = new MapData();
        Console.WriteLine($"  現在のデータ: {string.Join(", ", mapData.Get())}");

        Console.WriteLine("\nis_empty");
        bool output = mapData.IsEmpty();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nsize");
        int size = mapData.Size();
        Console.WriteLine($"  出力値: {size}");

        Console.WriteLine("\nadd");
        (string, int) input = ("apple", 100);
        Console.WriteLine($"  入力値: {input}");
        bool addOutput = mapData.Add(input.Item1, input.Item2);
        Console.WriteLine($"  出力値: {addOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", mapData.Get())}");

        Console.WriteLine("\nadd");
        input = ("banana", 150);
        Console.WriteLine($"  入力値: {input}");
        addOutput = mapData.Add(input.Item1, input.Item2);
        Console.WriteLine($"  出力値: {addOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", mapData.Get())}");

        Console.WriteLine("\nadd");
        input = ("apple", 200);
        Console.WriteLine($"  入力値: {input}");
        addOutput = mapData.Add(input.Item1, input.Item2);
        Console.WriteLine($"  出力値: {addOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", mapData.Get())}");

        Console.WriteLine("\nsize");
        size = mapData.Size();
        Console.WriteLine($"  出力値: {size}");

        Console.WriteLine("\nget");
        string getInput = "apple";
        Console.WriteLine($"  入力値: {getInput}");
        int? getOutput = mapData.GetValue(getInput);
        Console.WriteLine($"  出力値: {getOutput}");

        Console.WriteLine("\nget");
        getInput = "orange";
        Console.WriteLine($"  入力値: {getInput}");
        getOutput = mapData.GetValue(getInput);
        Console.WriteLine($"  出力値: {getOutput}");

        Console.WriteLine("\nupdate");
        input = ("banana", 180);
        Console.WriteLine($"  入力値: {input}");
        bool updateOutput = mapData.Update(input.Item1, input.Item2);
        Console.WriteLine($"  出力値: {updateOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", mapData.Get())}");

        Console.WriteLine("\nupdate");
        input = ("orange", 250);
        Console.WriteLine($"  入力値: {input}");
        updateOutput = mapData.Update(input.Item1, input.Item2);
        Console.WriteLine($"  出力値: {updateOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", mapData.Get())}");

        Console.WriteLine("\nget");
        getInput = "banana";
        getOutput = mapData.GetValue(getInput);
        Console.WriteLine($"  出力値: {getOutput}");

        Console.WriteLine("\nget_keys");
        var keysOutput = mapData.GetKeys();
        Console.WriteLine($"  出力値: {string.Join(", ", keysOutput)}");

        Console.WriteLine("\nvalues");
        var valuesOutput = mapData.GetValues();
        Console.WriteLine($"  出力値: {string.Join(", ", valuesOutput)}");

        Console.WriteLine("\nget_key");
        int keyInput = 180;
        Console.WriteLine($"  入力値: {keyInput}");
        string keyOutput = mapData.GetKey(keyInput);
        Console.WriteLine($"  出力値: {keyOutput}");

        Console.WriteLine("\nget_key");
        keyInput = 500;
        Console.WriteLine($"  入力値: {keyInput}");
        keyOutput = mapData.GetKey(keyInput);
        Console.WriteLine($"  出力値: {keyOutput}");

        Console.WriteLine("\nremove");
        string removeInput = "apple";
        Console.WriteLine($"  入力値: {removeInput}");
        bool removeOutput = mapData.Remove(removeInput);
        Console.WriteLine($"  出力値: {removeOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", mapData.Get())}");

        Console.WriteLine("\nremove");
        removeInput = "orange";
        Console.WriteLine($"  入力値: {removeInput}");
        removeOutput = mapData.Remove(removeInput);
        Console.WriteLine($"  出力値: {removeOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", mapData.Get())}");

        Console.WriteLine("\nsize");
        size = mapData.Size();
        Console.WriteLine($"  出力値: {size}");

        Console.WriteLine("\nget_keys");
        keysOutput = mapData.GetKeys();
        Console.WriteLine($"  出力値: {string.Join(", ", keysOutput)}");

        Console.WriteLine("\nclear");
        bool clearOutput = mapData.Clear();
        Console.WriteLine($"  出力値: {clearOutput}");
        Console.WriteLine($"  現在のデータ: {string.Join(", ", mapData.Get())}");

        Console.WriteLine("\nsize");
        size = mapData.Size();
        Console.WriteLine($"  出力値: {size}");

        Console.WriteLine("\nis_empty");
        output = mapData.IsEmpty();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nMap TEST <----- end");
    }
}
