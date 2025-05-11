document.addEventListener('DOMContentLoaded', function() {
    // 要素の取得
    const algorithmSelect = document.getElementById('algorithm');
    const searchValueInput = document.getElementById('search-value');
    const startBtn = document.getElementById('start-btn');
    const clearBtn = document.getElementById('clear-btn');
    const arrayContainer = document.getElementById('array-container');
    const hashTableContainer = document.getElementById('hash-table-container');
    const stepCounter = document.getElementById('step-counter');
    const stepDescription = document.getElementById('step-description');
    
    // 実行中フラグ
    let isRunning = false;
    
    // 配列の生成
    const arraySize = 10;
    let array = [];
    
    // ハッシュテーブルの生成
    const hashTableSize = 10;
    let hashTable = Array(hashTableSize).fill().map(() => []);
    
    // アニメーション遅延時間
    const delay = 1000;
    
    // アルゴリズムの初期化
    initializeArrayAndHashTable();
    
    // イベントリスナーの設定
    startBtn.addEventListener('click', startSearch);
    clearBtn.addEventListener('click', clearSearch);
    algorithmSelect.addEventListener('change', updateVisualization);
    
    // 検索アルゴリズムに応じた表示の更新
    function updateVisualization() {
        clearSearch();
        const algorithm = algorithmSelect.value;
        
        if (algorithm === 'hash') {
            arrayContainer.style.display = 'none';
            hashTableContainer.style.display = 'grid';
        } else {
            arrayContainer.style.display = 'flex';
            hashTableContainer.style.display = 'none';
        }
    }
    
    // 配列とハッシュテーブルの初期化
    function initializeArrayAndHashTable() {
        // 配列の初期化
        array = [];
        arrayContainer.innerHTML = '';
        
        for (let i = 0; i < arraySize; i++) {
            const value = Math.floor(Math.random() * 90) + 10; // 10-99の範囲で乱数生成
            array.push(value);
            
            // 配列要素の表示
            const element = document.createElement('div');
            element.className = 'array-element';
            element.textContent = value;
            
            const indexSpan = document.createElement('span');
            indexSpan.className = 'index';
            indexSpan.textContent = i;
            element.appendChild(indexSpan);
            
            arrayContainer.appendChild(element);
        }
        
        // 二分探索のために配列をソート
        if (algorithmSelect.value === 'binary') {
            array.sort((a, b) => a - b);
            
            // ソートされた配列を表示に反映
            const elements = arrayContainer.querySelectorAll('.array-element');
            elements.forEach((element, index) => {
                element.textContent = array[index];
                
                const indexSpan = document.createElement('span');
                indexSpan.className = 'index';
                indexSpan.textContent = index;
                element.appendChild(indexSpan);
            });
        }
        
        // ハッシュテーブルの初期化
        hashTable = Array(hashTableSize).fill().map(() => []);
        hashTableContainer.innerHTML = '';
        
        // ハッシュテーブルの表示
        for (let i = 0; i < hashTableSize; i++) {
            const bucket = document.createElement('div');
            bucket.className = 'hash-bucket';
            
            const bucketIndex = document.createElement('div');
            bucketIndex.className = 'bucket-index';
            bucketIndex.textContent = `バケット ${i}`;
            bucket.appendChild(bucketIndex);
            
            const bucketItems = document.createElement('div');
            bucketItems.className = 'bucket-items';
            bucket.appendChild(bucketItems);
            
            hashTableContainer.appendChild(bucket);
        }
        
        // ハッシュテーブルにアイテムを追加
        if (algorithmSelect.value === 'hash') {
            for (let i = 0; i < array.length; i++) {
                const value = array[i];
                const hashIndex = hashFunction(value);
                hashTable[hashIndex].push(value);
                
                const bucketItems = hashTableContainer.querySelectorAll('.bucket-items')[hashIndex];
                const item = document.createElement('div');
                item.className = 'bucket-item';
                item.textContent = value;
                bucketItems.appendChild(item);
            }
        }
        
        // ステップ情報のリセット
        stepCounter.textContent = '0';
        stepDescription.textContent = 'アルゴリズムを選択して実行ボタンを押してください。';
    }
    
    // ハッシュ関数
    function hashFunction(value) {
        return value % hashTableSize;
    }
    
    // 検索開始
    function startSearch() {
        if (isRunning) return;
        isRunning = true;
        
        const algorithm = algorithmSelect.value;
        const searchValue = parseInt(searchValueInput.value);
        
        // 配列要素のクラスをリセット
        const arrayElements = document.querySelectorAll('.array-element');
        arrayElements.forEach(element => {
            element.className = 'array-element';
        });
        
        // ハッシュバケットのクラスをリセット
        const hashBuckets = document.querySelectorAll('.hash-bucket');
        hashBuckets.forEach(bucket => {
            bucket.className = 'hash-bucket';
        });
        
        // ステップカウンターをリセット
        let step = 0;
        stepCounter.textContent = step;
        
        // アルゴリズムに応じた検索を実行
        switch (algorithm) {
            case 'linear':
                linearSearch(searchValue, step);
                break;
            case 'binary':
                binarySearch(searchValue, step);
                break;
            case 'hash':
                hashSearch(searchValue, step);
                break;
        }
    }
    
    // 線形探索
    function linearSearch(searchValue, step) {
        const arrayElements = arrayContainer.querySelectorAll('.array-element');
        
        function searchStep(index) {
            if (index >= array.length) {
                // 要素が見つからなかった場合
                step++;
                stepCounter.textContent = step;
                stepDescription.textContent = `値 ${searchValue} は配列内に見つかりませんでした。`;
                isRunning = false;
                return;
            }
            
            // 現在のステップを更新
            step++;
            stepCounter.textContent = step;
            
            // 前の要素のハイライトを解除
            if (index > 0) {
                arrayElements[index - 1].classList.remove('current');
            }
            
            // 現在の要素をハイライト
            arrayElements[index].classList.add('current');
            
            // 現在のステップの説明を更新
            stepDescription.textContent = `インデックス ${index} の値 ${array[index]} を検証中...`;
            
            setTimeout(() => {
                if (array[index] === searchValue) {
                    // 要素が見つかった場合
                    arrayElements[index].classList.remove('current');
                    arrayElements[index].classList.add('found');
                    stepDescription.textContent = `値 ${searchValue} がインデックス ${index} で見つかりました！`;
                    isRunning = false;
                } else {
                    // 次のステップへ
                    searchStep(index + 1);
                }
            }, delay);
        }
        
        // 探索開始
        searchStep(0);
    }
    
    // 二分探索
    function binarySearch(searchValue, step) {
        const arrayElements = arrayContainer.querySelectorAll('.array-element');
        
        function searchStep(left, right) {
            // 探索範囲が無効になった場合
            if (left > right) {
                step++;
                stepCounter.textContent = step;
                stepDescription.textContent = `値 ${searchValue} は配列内に見つかりませんでした。`;
                
                // すべての要素のハイライトを解除
                arrayElements.forEach(element => {
                    element.className = 'array-element';
                });
                
                isRunning = false;
                return;
            }
            
            // 現在のステップを更新
            step++;
            stepCounter.textContent = step;
            
            // すべての要素のハイライトを解除
            arrayElements.forEach(element => {
                element.className = 'array-element';
            });
            
            // 左端と右端をハイライト
            arrayElements[left].classList.add('left');
            arrayElements[right].classList.add('right');
            
            // 中央値を計算
            const mid = Math.floor((left + right) / 2);
            
            // 中央値をハイライト
            arrayElements[mid].classList.add('mid');
            
            // 現在のステップの説明を更新
            stepDescription.textContent = `左端=${left}, 右端=${right}, 中央=${mid} の値 ${array[mid]} を検証中...`;
            
            setTimeout(() => {
                if (array[mid] === searchValue) {
                    // 要素が見つかった場合
                    arrayElements.forEach(element => {
                        element.className = 'array-element';
                    });
                    arrayElements[mid].classList.add('found');
                    stepDescription.textContent = `値 ${searchValue} がインデックス ${mid} で見つかりました！`;
                    isRunning = false;
                } else if (array[mid] > searchValue) {
                    // 左半分を探索
                    stepDescription.textContent = `${array[mid]} > ${searchValue} なので、左半分を探索します。`;
                    setTimeout(() => {
                        searchStep(left, mid - 1);
                    }, delay);
                } else {
                    // 右半分を探索
                    stepDescription.textContent = `${array[mid]} < ${searchValue} なので、右半分を探索します。`;
                    setTimeout(() => {
                        searchStep(mid + 1, right);
                    }, delay);
                }
            }, delay);
        }
        
        // 探索開始
        searchStep(0, array.length - 1);
    }
    
    // ハッシュ探索
    function hashSearch(searchValue, step) {
        const hashBuckets = hashTableContainer.querySelectorAll('.hash-bucket');
        
        // ハッシュ値を計算
        const hashIndex = hashFunction(searchValue);
        
        // 現在のステップを更新
        step++;
        stepCounter.textContent = step;
        
        // 現在のバケットをハイライト
        hashBuckets[hashIndex].classList.add('current');
        
        // 現在のステップの説明を更新
        stepDescription.textContent = `ハッシュ関数: h(${searchValue}) = ${searchValue} % ${hashTableSize} = ${hashIndex} を計算`;
        
        setTimeout(() => {
            step++;
            stepCounter.textContent = step;
            
            // バケット内のアイテムを確認
            const bucket = hashTable[hashIndex];
            
            if (bucket.length === 0) {
                // バケットが空の場合
                stepDescription.textContent = `バケット ${hashIndex} は空です。値 ${searchValue} は見つかりませんでした。`;
                hashBuckets[hashIndex].classList.remove('current');
                isRunning = false;
                return;
            }
            
            stepDescription.textContent = `バケット ${hashIndex} を探索中...`;
            
            function checkBucketItem(itemIndex) {
                if (itemIndex >= bucket.length) {
                    // バケット内に要素が見つからなかった場合
                    step++;
                    stepCounter.textContent = step;
                    stepDescription.textContent = `バケット ${hashIndex} 内に値 ${searchValue} は見つかりませんでした。`;
                    hashBuckets[hashIndex].classList.remove('current');
                    isRunning = false;
                    return;
                }
                
                // 現在のステップを更新
                step++;
                stepCounter.textContent = step;
                
                const bucketItems = hashBuckets[hashIndex].querySelectorAll('.bucket-item');
                
                // 前のアイテムのハイライトを解除
                if (itemIndex > 0) {
                    bucketItems[itemIndex - 1].style.backgroundColor = '';
                }
                
                // 現在のアイテムをハイライト
                bucketItems[itemIndex].style.backgroundColor = '#ffeb3b';
                
                // 現在のステップの説明を更新
                stepDescription.textContent = `バケット ${hashIndex} のアイテム [${itemIndex}]: ${bucket[itemIndex]} を検証中...`;
                
                setTimeout(() => {
                    if (bucket[itemIndex] === searchValue) {
                        // 要素が見つかった場合
                        hashBuckets[hashIndex].classList.remove('current');
                        hashBuckets[hashIndex].classList.add('found');
                        bucketItems[itemIndex].style.backgroundColor = '#66bb6a';
                        stepDescription.textContent = `値 ${searchValue} がバケット ${hashIndex} のアイテム [${itemIndex}] で見つかりました！`;
                        isRunning = false;
                    } else {
                        // 次のアイテムへ
                        checkBucketItem(itemIndex + 1);
                    }
                }, delay);
            }
            
            // バケット内の探索開始
            checkBucketItem(0);
            
        }, delay);
    }
    
    // 検索のクリア
    function clearSearch() {
        isRunning = false;
        initializeArrayAndHashTable();
        updateVisualization();
    }
    
    // 初期表示の設定
    updateVisualization();
});