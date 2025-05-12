// JavaScript
// データ構造: 木 (Tree)

class NodeData {
    constructor(value) {
        this._value = value;
        this._parent = null;
        this._children = [];
    }
    
    get_value() {
        return this._value;
    }

    get_parent() {
        return this._parent;
    }

    get_children() {
        return this._children;
    }

    set_parent(parent) {
        this._parent = parent;
        return true;
    }

    add_child(child) {
        child.set_parent(this);
        this._children.push(child);
        return true;
    }
    
    remove_child(child) {
        const index = this._children.indexOf(child);
        if (index !== -1) {
            child.set_parent(null);
            this._children.splice(index, 1);
            return true;
        } else {
            return false;
        }
    }
    
    is_leaf() {
        return this._children.length === 0;
    }
}

class TreeData {
    constructor() {
        this._data = null;
    }

    get() {
        return this._data;
    }
    
    get_height(node = null) {
        if (node === null) {
            node = this._data;
        }
        if (node === null) {
            return 0;
        }
        if (node.is_leaf()) {
            return 1;
        }
        return 1 + Math.max(...node.get_children().map(child => this.get_height(child)));
    }
    
    get_parent(node) {
        return node.get_parent();
    }
    
    get_children(node) {
        return node.get_children();
    }

    get_node(value, node = null) {
        if (node === null) {
            node = this._data;
        }
        if (node === null) {
            return null;
        }
        
        if (node.get_value() === value) {
            return node;
        }
        
        for (const child of node.get_children()) {
            const result = this.get_node(value, child);
            if (result !== null) {
                return result;
            }
        }
        
        return null;
    }

    add(parent, value) {
        const new_node = new NodeData(value);
        if (parent === null) {
            if (this._data === null) {
                this._data = new_node;
                return true;
            } else {
                console.log(`ERROR: ${value} 重複です`);
                return false;
            }
        } else {
            parent.add_child(new_node);
            return true;
        }
    }

    remove(node) {
        if (node === null) {
            return false;
        }
        
        if (node === this._data) {
            this._data = null;
            return true;
        }
        
        const parent = node.get_parent();
        if (parent !== null) {
            return parent.remove_child(node);
        }
        
        return false;
    }
    
    traverse(node = null, mode = "pre-order") {
        if (node === null) {
            node = this._data;
        }
        if (node === null) {
            return [];
        }
        
        const result = [];
        
        if (mode === "pre-order") {
            result.push(node.get_value());
            for (const child of node.get_children()) {
                result.push(...this.traverse(child, mode));
            }
        } else if (mode === "post-order") {
            for (const child of node.get_children()) {
                result.push(...this.traverse(child, mode));
            }
            result.push(node.get_value());
        } else if (mode === "level-order") {
            const queue = [node];
            while (queue.length > 0) {
                const current = queue.shift();
                result.push(current.get_value());
                queue.push(...current.get_children());
            }
        }
        
        return result;
    }
    
    is_leaf(node) {
        return node !== null && node.is_leaf();
    }
    
    is_empty() {
        return this._data === null;
    }
    
    size(node = null) {
        if (node === null) {
            node = this._data;
        }
        if (node === null) {
            return 0;
        }
        
        let count = 1;
        for (const child of node.get_children()) {
            count += this.size(child);
        }
        
        return count;
    }
    
    clear() {
        this._data = null;
        return true;
    }

    display() {
        if (this._data === null) {
            return [];
        }
        
        return this.traverse(null, "level-order");
    }
}

function main() {
    console.log("Tree TEST -----> start");

    console.log("\nnew");
    const tree_data = new TreeData();
    console.log(`  現在のデータ: ${tree_data.display()}`);

    console.log("\nis_empty");
    const output1 = tree_data.is_empty();
    console.log(`  出力値: ${output1}`);

    console.log("\nsize");
    const output2 = tree_data.size();
    console.log(`  出力値: ${output2}`);

    console.log("\nadd");
    const input_params1 = [null, 'Root'];
    console.log(`  入力値: ${input_params1}`);
    const output3 = tree_data.add(...input_params1);
    console.log(`  出力値: ${output3}`);
    console.log(`  現在のデータ: ${tree_data.display()}`);
    
    const root_node = tree_data.get();

    console.log("\nadd");
    const input_params2 = [root_node, 'Child1'];
    console.log(`  入力値: ${input_params2}`);
    const output4 = tree_data.add(...input_params2);
    console.log(`  出力値: ${output4}`);
    console.log(`  現在のデータ: ${tree_data.display()}`);

    console.log("\nadd");
    const input_params3 = [root_node, 'Child2'];
    console.log(`  入力値: ${input_params3}`);
    const output5 = tree_data.add(...input_params3);
    console.log(`  出力値: ${output5}`);
    console.log(`  現在のデータ: ${tree_data.display()}`);

    console.log("\nget_node");
    const input_value = 'Child1';
    console.log(`  入力値: ${input_value}`);
    const output6 = tree_data.get_node(input_value);
    console.log(`  出力値: ${output6}`);
    console.log(`  現在のデータ: ${tree_data.display()}`);

    console.log("\ntraverse");
    const input_mode = 'pre-order';
    console.log(`  入力値: ${input_mode}`);
    const output7 = tree_data.traverse(null, input_mode);
    console.log(`  出力値: ${output7}`);
    console.log(`  現在のデータ: ${tree_data.display()}`);

    console.log("\nTree TEST <----- end");
}

main();