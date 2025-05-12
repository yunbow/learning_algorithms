// JavaScript
// データ構造: マップ (Map)

class MapData {
    constructor() {
        this._data = new Map();
    }

    get() {
        return Array.from(this._data.entries());
    }
    
    get_keys() {
        return Array.from(this._data.keys());
    }
    
    get_values() {
        return Array.from(this._data.values());
    }
    
    get_key(value) {
        for (const [k, v] of this._data.entries()) {
            if (v === value) {
                return k;
            }
        }
        console.log(`ERROR: ${value} は範囲外です`);
        return null;
    }
    
    get_value(key) {
        if (this._data.has(key)) {
            return this._data.get(key);
        }
        console.log(`ERROR: ${key} は範囲外です`);
        return null;
    }

    add(key, value) {
        if (this._data.has(key)) {
            console.log(`ERROR: ${key} は重複です`);
            return false;
        }
        this._data.set(key, value);
        return true;
    }
    
    remove(key) {
        if (this._data.has(key)) {
            this._data.delete(key);
            return true;
        }
        console.log(`ERROR: ${key} は範囲外です`);
        return false;
    }
    
    update(key, value) {
        if (this._data.has(key)) {
            this._data.set(key, value);
            return true;
        }
        console.log(`ERROR: ${key} は範囲外です`);
        return false;
    }
    
    is_empty() {
        return this._data.size === 0;
    }
    
    size() {
        return this._data.size;
    }
    
    clear() {
        this._data.clear();
        return true;
    }
}

function main() {
    console.log("Map TEST -----> start");

    console.log("\nnew");
    const map_data = new MapData();
    console.log(`  現在のデータ: ${JSON.stringify(map_data.get())}`);

    console.log("\nis_empty");
    const empty_output = map_data.is_empty();
    console.log(`  出力値: ${empty_output}`);

    console.log("\nsize");
    const size_output = map_data.size();
    console.log(`  出力値: ${size_output}`);

    console.log("\nadd");
    const input1 = ['apple', 100];
    console.log(`  入力値: ${JSON.stringify(input1)}`);
    const add_output1 = map_data.add(...input1);
    console.log(`  出力値: ${add_output1}`);
    console.log(`  現在のデータ: ${JSON.stringify(map_data.get())}`);

    console.log("\nadd");
    const input2 = ['banana', 150];
    console.log(`  入力値: ${JSON.stringify(input2)}`);
    const add_output2 = map_data.add(...input2);
    console.log(`  出力値: ${add_output2}`);
    console.log(`  現在のデータ: ${JSON.stringify(map_data.get())}`);

    console.log("\nadd");
    const input3 = ['apple', 200];
    console.log(`  入力値: ${JSON.stringify(input3)}`);
    const add_output3 = map_data.add(...input3);
    console.log(`  出力値: ${add_output3}`);
    console.log(`  現在のデータ: ${JSON.stringify(map_data.get())}`);

    console.log("\nsize");
    const size_output2 = map_data.size();
    console.log(`  出力値: ${size_output2}`);

    console.log("\nget");
    const get_input1 = 'apple';
    console.log(`  入力値: ${get_input1}`);
    const get_output1 = map_data.get_value(get_input1);
    console.log(`  出力値: ${get_output1}`);

    console.log("\nget");
    const get_input2 = 'orange';
    console.log(`  入力値: ${get_input2}`);
    const get_output2 = map_data.get_value(get_input2);
    console.log(`  出力値: ${get_output2}`);

    console.log("\nupdate");
    const update_input1 = ['banana', 180];
    console.log(`  入力値: ${JSON.stringify(update_input1)}`);
    const update_output1 = map_data.update(...update_input1);
    console.log(`  出力値: ${update_output1}`);
    console.log(`  現在のデータ: ${JSON.stringify(map_data.get())}`);

    console.log("\nupdate");
    const update_input2 = ['orange', 250];
    console.log(`  入力値: ${JSON.stringify(update_input2)}`);
    const update_output2 = map_data.update(...update_input2);
    console.log(`  出力値: ${update_output2}`);
    console.log(`  現在のデータ: ${JSON.stringify(map_data.get())}`);

    console.log("\nget");
    const get_input3 = 'banana';
    const get_output3 = map_data.get_value(get_input3);
    console.log(`  出力値: ${get_output3}`);

    console.log("\nget_keys");
    const keys_output = map_data.get_keys();
    console.log(`  出力値: ${JSON.stringify(keys_output)}`);

    console.log("\nvalues");
    const values_output = map_data.get_values();
    console.log(`  出力値: ${JSON.stringify(values_output)}`);

    console.log("\nget_key");
    const get_key_input1 = 180;
    console.log(`  入力値: ${get_key_input1}`);
    const get_key_output1 = map_data.get_key(get_key_input1);
    console.log(`  出力値: ${get_key_output1}`);

    console.log("\nget_key");
    const get_key_input2 = 500;
    console.log(`  入力値: ${get_key_input2}`);
    const get_key_output2 = map_data.get_key(get_key_input2);
    console.log(`  出力値: ${get_key_output2}`);

    console.log("\nremove");
    const remove_input1 = 'apple';
    console.log(`  入力値: ${remove_input1}`);
    const remove_output1 = map_data.remove(remove_input1);
    console.log(`  出力値: ${remove_output1}`);
    console.log(`  現在のデータ: ${JSON.stringify(map_data.get())}`);

    console.log("\nremove");
    const remove_input2 = 'orange';
    console.log(`  入力値: ${remove_input2}`);
    const remove_output2 = map_data.remove(remove_input2);
    console.log(`  出力値: ${remove_output2}`);
    console.log(`  現在のデータ: ${JSON.stringify(map_data.get())}`);

    console.log("\nsize");
    const size_output3 = map_data.size();
    console.log(`  出力値: ${size_output3}`);

    console.log("\nget_keys");
    const keys_output2 = map_data.get_keys();
    console.log(`  出力値: ${JSON.stringify(keys_output2)}`);

    console.log("\nclear");
    const clear_output = map_data.clear();
    console.log(`  出力値: ${clear_output}`);
    console.log(`  現在のデータ: ${JSON.stringify(map_data.get())}`);

    console.log("\nsize");
    const size_output4 = map_data.size();
    console.log(`  出力値: ${size_output4}`);

    console.log("\nis_empty");
    const is_empty_output = map_data.is_empty();
    console.log(`  出力値: ${is_empty_output}`);

    console.log("\nMap TEST <----- end");
}

// Uncomment the following line to run the main function
// main();