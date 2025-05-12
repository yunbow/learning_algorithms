// JavaScript
// データ構造: スタック (Stack)

class StackData {
    constructor() {
        this._data = [];
    }

    get() {
        return this._data;
    }

    get_index(item) {
        try {
            const index = this._data.indexOf(item);
            if (index === -1) {
                console.log(`ERROR: ${item} は範囲外です`);
                return -1;
            }
            return index;
        } catch (error) {
            console.log(`ERROR: ${item} は範囲外です`);
            return -1;
        }
    }

    get_value(index) {
        if (0 <= index && index < this._data.length) {
            return this._data[index];
        } else {
            console.log(`ERROR: ${index} は範囲外です`);
            return null;
        }
    }

    push(item) {
        this._data.push(item);
        return true;
    }

    pop() {
        if (!this.is_empty()) {
            this._data.pop();
            return true;
        } else {
            console.log(`ERROR: 空です`);
            return false;
        }
    }

    peek() {
        if (!this.is_empty()) {
            return this._data[this._data.length - 1];
        } else {
            return null;
        }
    }

    is_empty() {
        return this._data.length === 0;
    }

    size() {
        return this._data.length;
    }

    clear() {
        this._data = [];
        return true;
    }
}

function main() {
    console.log("Stack TEST -----> start");

    console.log("\nnew");
    const stack_data = new StackData();
    console.log(`  現在のデータ: ${JSON.stringify(stack_data.get())}`);

    console.log("\nis_empty");
    let output = stack_data.is_empty();
    console.log(`  出力値: ${output}`);

    console.log("\nsize");
    output = stack_data.size();
    console.log(`  出力値: ${output}`);

    console.log("\npush");
    const items_to_push = [10, 20, 30, 40];
    for (const item of items_to_push) {
        console.log(`  入力値: ${item}`);
        output = stack_data.push(item);
        console.log(`  出力値: ${output}`);
        console.log(`  現在のデータ: ${JSON.stringify(stack_data.get())}`);
    }

    console.log("\nsize");
    output = stack_data.size();
    console.log(`  出力値: ${output}`);

    console.log("\nis_empty");
    output = stack_data.is_empty();
    console.log(`  出力値: ${output}`);

    console.log("\npeek");
    output = stack_data.peek();
    console.log(`  出力値: ${output}`);

    console.log("\nget_index");
    let input = 30;
    console.log(`  入力値: ${input}`);
    output = stack_data.get_index(input);
    console.log(`  出力値: ${output}`);

    console.log("\nget_index");
    input = 50;
    console.log(`  入力値: ${input}`);
    output = stack_data.get_index(input);
    console.log(`  出力値: ${output}`);

    console.log("\npop");
    while (!stack_data.is_empty()) {
        output = stack_data.pop();
        console.log(`  出力値: ${output}`);
        console.log(`  現在のデータ: ${JSON.stringify(stack_data.get())}`);
    }

    console.log("\nis_empty");
    output = stack_data.is_empty();
    console.log(`  出力値: ${output}`);

    console.log("\nsize");
    output = stack_data.size();
    console.log(`  出力値: ${output}`);

    console.log("\npop");
    output = stack_data.pop();
    console.log(`  出力値: ${output}`);

    console.log("\npeek");
    output = stack_data.peek();
    console.log(`  出力値: ${output}`);

    console.log("\nStack TEST <----- end");
}

main();