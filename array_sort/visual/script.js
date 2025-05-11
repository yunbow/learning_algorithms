document.addEventListener('DOMContentLoaded', function() {
    // DOM要素
    const algorithmSelect = document.getElementById('algorithm');
    const startButton = document.getElementById('start');
    const clearButton = document.getElementById('clear');
    const arrayContainer = document.getElementById('array-container');
    const stepCountElement = document.getElementById('step-count');
    const descriptionTextElement = document.getElementById('description-text');

    // 状態管理
    let array = [];
    let animationSpeed = 500; // ミリ秒
    let isSorting = false;
    let stepCount = 0;
    
    // 初期配列を生成
    initializeArray();

    // イベントリスナー
    startButton.addEventListener('click', startSorting);
    clearButton.addEventListener('click', resetArray);

    // 配列の初期化
    function initializeArray() {
        array = [];
        // 10個のランダムな値を持つ配列を生成（値の範囲: 10-100）
        for (let i = 0; i < 10; i++) {
            array.push(Math.floor(Math.random() * 91) + 10);
        }
        renderArray();
        stepCount = 0;
        updateStepCount();
        descriptionTextElement.textContent = "アルゴリズムを選択して実行ボタンをクリックしてください。";
    }

    // 配列の描画
    function renderArray(comparingIndices = [], swappingIndices = [], sortedIndices = [], pivotIndex = -1) {
        arrayContainer.innerHTML = '';
        
        array.forEach((value, index) => {
            const bar = document.createElement('div');
            bar.className = 'array-bar';
            bar.style.height = `${value * 2}px`;
            
            // バーの状態に応じたクラスを追加
            if (sortedIndices.includes(index)) {
                bar.classList.add('sorted');
            } else if (swappingIndices.includes(index)) {
                bar.classList.add('swapping');
            } else if (comparingIndices.includes(index)) {
                bar.classList.add('comparing');
            } else if (index === pivotIndex) {
                bar.classList.add('pivot');
            }
            
            // バーの値を表示
            const valueLabel = document.createElement('span');
            valueLabel.textContent = value;
            valueLabel.style.position = 'relative';
            valueLabel.style.top = '-25px';
            bar.appendChild(valueLabel);
            
            arrayContainer.appendChild(bar);
        });
    }

    // ステップカウントの更新
    function updateStepCount() {
        stepCountElement.textContent = stepCount;
    }

    // 説明の更新
    function updateDescription(text) {
        descriptionTextElement.textContent = text;
    }

    // ソート開始
    function startSorting() {
        if (isSorting) return;
        
        const selectedAlgorithm = algorithmSelect.value;
        isSorting = true;
        stepCount = 0;
        
        switch (selectedAlgorithm) {
            case 'bubble':
                bubbleSort();
                break;
            case 'selection':
                selectionSort();
                break;
            case 'insertion':
                insertionSort();
                break;
            case 'quick':
                quickSortWrapper();
                break;
            case 'merge':
                mergeSortWrapper();
                break;
        }
    }

    // 配列のリセット
    function resetArray() {
        if (isSorting) return;
        initializeArray();
    }

    // スリープ関数（アニメーション用）
    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    // 配列の要素を交換
    async function swap(i, j) {
        const temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // バブルソート
    async function bubbleSort() {
        const n = array.length;
        let sorted = false;
        
        for (let i = 0; i < n - 1 && !sorted; i++) {
            sorted = true;
            for (let j = 0; j < n - i - 1; j++) {
                stepCount++;
                updateStepCount();
                updateDescription(`${array[j]} と ${array[j+1]} を比較中`);
                renderArray([j, j+1]);
                await sleep(animationSpeed);
                
                if (array[j] > array[j+1]) {
                    updateDescription(`${array[j]} > ${array[j+1]} なので交換`);
                    renderArray([], [j, j+1]);
                    await sleep(animationSpeed);
                    
                    swap(j, j+1);
                    sorted = false;
                    
                    renderArray([], [j, j+1]);
                    await sleep(animationSpeed);
                }
            }
            
            // 各パスの終了後に最後の要素をソート済みとしてマーク
            const sortedIndices = [];
            for (let k = n - 1; k >= n - i - 1; k--) {
                sortedIndices.push(k);
            }
            renderArray([], [], sortedIndices);
            await sleep(animationSpeed);
        }
        
        // ソート完了
        completeSort();
    }

    // 選択ソート
    async function selectionSort() {
        const n = array.length;
        const sortedIndices = [];
        
        for (let i = 0; i < n - 1; i++) {
            let minIndex = i;
            
            updateDescription(`インデックス ${i} から最小値を探索中`);
            renderArray([i]);
            await sleep(animationSpeed);
            
            for (let j = i + 1; j < n; j++) {
                stepCount++;
                updateStepCount();
                
                updateDescription(`${array[minIndex]} と ${array[j]} を比較中`);
                renderArray([minIndex, j], [], sortedIndices);
                await sleep(animationSpeed);
                
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                    updateDescription(`新しい最小値: ${array[minIndex]}`);
                    renderArray([minIndex], [], sortedIndices);
                    await sleep(animationSpeed);
                }
            }
            
            if (minIndex !== i) {
                updateDescription(`${array[i]} と ${array[minIndex]} を交換`);
                renderArray([], [i, minIndex], sortedIndices);
                await sleep(animationSpeed);
                
                swap(i, minIndex);
                
                renderArray([], [i, minIndex], sortedIndices);
                await sleep(animationSpeed);
            }
            
            sortedIndices.push(i);
            renderArray([], [], sortedIndices);
            await sleep(animationSpeed);
        }
        
        sortedIndices.push(n - 1);
        renderArray([], [], sortedIndices);
        await sleep(animationSpeed);
        
        // ソート完了
        completeSort();
    }

    // 挿入ソート
    async function insertionSort() {
        const n = array.length;
        const sortedIndices = [0]; // 最初の要素はすでにソート済み
        
        for (let i = 1; i < n; i++) {
            const key = array[i];
            let j = i - 1;
            
            updateDescription(`キー値 ${key} を適切な位置に挿入`);
            renderArray([i], [], sortedIndices);
            await sleep(animationSpeed);
            
            while (j >= 0 && array[j] > key) {
                stepCount++;
                updateStepCount();
                
                updateDescription(`${array[j]} > ${key} なので、${array[j]} を右にシフト`);
                renderArray([j], [j, j+1], sortedIndices);
                await sleep(animationSpeed);
                
                array[j + 1] = array[j];
                j--;
                
                const currentSorted = [...sortedIndices];
                renderArray([j+1], [], currentSorted);
                await sleep(animationSpeed);
            }
            
            array[j + 1] = key;
            
            updateDescription(`キー値 ${key} をインデックス ${j+1} に挿入完了`);
            sortedIndices.push(i);
            renderArray([], [], sortedIndices);
            await sleep(animationSpeed);
        }
        
        // ソート完了
        completeSort();
    }

    // クイックソートのラッパー
    async function quickSortWrapper() {
        const sortedIndices = [];
        await quickSort(0, array.length - 1, sortedIndices);
        completeSort();
    }

    // クイックソート
    async function quickSort(low, high, sortedIndices) {
        if (low < high) {
            const pivotIndex = await partition(low, high, sortedIndices);
            sortedIndices.push(pivotIndex);
            renderArray([], [], sortedIndices, -1);
            await sleep(animationSpeed);
            
            await quickSort(low, pivotIndex - 1, sortedIndices);
            await quickSort(pivotIndex + 1, high, sortedIndices);
        } else if (low === high) {
            sortedIndices.push(low);
            renderArray([], [], sortedIndices, -1);
            await sleep(animationSpeed);
        }
    }

    // クイックソートのパーティション
    async function partition(low, high, sortedIndices) {
        const pivot = array[high];
        updateDescription(`ピボット値: ${pivot}`);
        renderArray([], [], sortedIndices, high);
        await sleep(animationSpeed);
        
        let i = low - 1;
        
        for (let j = low; j < high; j++) {
            stepCount++;
            updateStepCount();
            
            updateDescription(`${array[j]} と ピボット ${pivot} を比較中`);
            renderArray([j], [], sortedIndices, high);
            await sleep(animationSpeed);
            
            if (array[j] <= pivot) {
                i++;
                
                if (i !== j) {
                    updateDescription(`${array[i]} と ${array[j]} を交換`);
                    renderArray([], [i, j], sortedIndices, high);
                    await sleep(animationSpeed);
                    
                    swap(i, j);
                    
                    renderArray([], [i, j], sortedIndices, high);
                    await sleep(animationSpeed);
                }
            }
        }
        
        if (i + 1 !== high) {
            updateDescription(`${array[i+1]} と ピボット ${pivot} を交換`);
            renderArray([], [i+1, high], sortedIndices);
            await sleep(animationSpeed);
            
            swap(i + 1, high);
            
            renderArray([], [i+1, high], sortedIndices);
            await sleep(animationSpeed);
        }
        
        return i + 1;
    }

    // マージソートのラッパー
    async function mergeSortWrapper() {
        const sortedIndices = [];
        const auxiliaryArray = [...array];
        await mergeSort(0, array.length - 1, auxiliaryArray, sortedIndices);
        completeSort();
    }

    // マージソート
    async function mergeSort(left, right, auxiliaryArray, sortedIndices) {
        if (left >= right) return;
        
        const mid = Math.floor((left + right) / 2);
        
        updateDescription(`配列を分割: [${left}...${mid}] と [${mid+1}...${right}]`);
        renderArray([left, mid, right]);
        await sleep(animationSpeed);
        
        await mergeSort(left, mid, auxiliaryArray, sortedIndices);
        await mergeSort(mid + 1, right, auxiliaryArray, sortedIndices);
        
        await merge(left, mid, right, auxiliaryArray, sortedIndices);
    }

    // マージ操作
    async function merge(left, mid, right, auxiliaryArray, sortedIndices) {
        updateDescription(`[${left}...${mid}] と [${mid+1}...${right}] をマージ`);
        
        // 元の配列をコピー
        for (let i = left; i <= right; i++) {
            auxiliaryArray[i] = array[i];
        }
        
        let i = left;      // 左半分の開始インデックス
        let j = mid + 1;   // 右半分の開始インデックス
        let k = left;      // マージされた配列の現在位置
        
        while (i <= mid && j <= right) {
            stepCount++;
            updateStepCount();
            
            updateDescription(`${auxiliaryArray[i]} と ${auxiliaryArray[j]} を比較中`);
            renderArray([i, j], [], sortedIndices);
            await sleep(animationSpeed);
            
            if (auxiliaryArray[i] <= auxiliaryArray[j]) {
                array[k] = auxiliaryArray[i];
                i++;
            } else {
                array[k] = auxiliaryArray[j];
                j++;
            }
            
            renderArray([], [k], sortedIndices);
            await sleep(animationSpeed);
            k++;
        }
        
        // 残りの要素をコピー
        while (i <= mid) {
            stepCount++;
            updateStepCount();
            
            updateDescription(`残りの左側の要素 ${auxiliaryArray[i]} をコピー`);
            array[k] = auxiliaryArray[i];
            
            renderArray([], [k], sortedIndices);
            await sleep(animationSpeed);
            
            i++;
            k++;
        }
        
        while (j <= right) {
            stepCount++;
            updateStepCount();
            
            updateDescription(`残りの右側の要素 ${auxiliaryArray[j]} をコピー`);
            array[k] = auxiliaryArray[j];
            
            renderArray([], [k], sortedIndices);
            await sleep(animationSpeed);
            
            j++;
            k++;
        }
        
        // マージされた部分をソート済みとしてマーク
        for (let i = left; i <= right; i++) {
            if (!sortedIndices.includes(i)) {
                sortedIndices.push(i);
            }
        }
        
        updateDescription(`インデックス [${left}...${right}] のマージ完了`);
        renderArray([], [], sortedIndices);
        await sleep(animationSpeed);
    }

    // ソート完了
    function completeSort() {
        isSorting = false;
        renderArray([], [], Array.from({ length: array.length }, (_, i) => i));
        updateDescription("ソート完了！");
    }
});
