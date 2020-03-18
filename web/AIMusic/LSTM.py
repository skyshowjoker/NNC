import os
import time
import json
import numpy as np
import tensorflow as tf
#如果装了tensorflow2.0以上的版本就注释掉上面一行，换用下面两行
#import tensorflow.compat.v1 as tf
#tf.disable_v2_behavior()
num=405
def data_measure(file_name):
    with open(file_name) as f:
        text=f.read()
    content=text.split('@')
    content=content[:num]
    #把除了第一首之外的所有音乐开头的回车给去掉
    for i in range(1, 405):
        content[i] = content[i][1:]

    for i in range(8):
        content[i]='<'+content[i]+'>'
    #print(content)
    all_char=[]
    for con_i in content:
        all_char+=[char for char in con_i]

    all_char=sorted(set(all_char),key=all_char.index)#Python 去除列表中重复的元素
    all_char.append('?')#填充至一样长度用的,不过目前不需要

    char_to_int={c: i for i, c in enumerate(all_char)}#enumerate() 函数用于将一个可遍历的数据对象(如列表、元组或字符串)组合为一个索引序列，同时列出数据和数据下标，一般用在 for 循环当中。
    int_to_char=dict(enumerate(all_char))

    content_vec=[list(map(lambda word: char_to_int.get(word, len(all_char)), a_music)) for a_music in content]
    #print(char_to_int)#<:0    >:29
    #print(content_vec)
    return content_vec,char_to_int,all_char,int_to_char
def get_batches(batch_size,content_vec,char_to_int):
   n_chunk=len(content_vec)//batch_size
   x_batches=[]
   y_batches=[]
   for i in range(n_chunk):
       start_idx=i*batch_size
       end_idx=start_idx+batch_size

       batches=content_vec[start_idx:end_idx]
       lenght=max(map(len,batches))
       x_data=np.full((batch_size,lenght),char_to_int['?'],np.int32)
       for row in range(batch_size):
           x_data[row,:len(batches[row])]=batches[row]
       y_data=np.copy(x_data)
       y_data[:,:-1]=x_data[:,1:]
       x_batches.append(x_data)
       y_batches.append(y_data)
   return x_batches,y_batches
def build_lstm(lstm_size, num_layers, batch_size, keep_prob):
    cell_list=[]
    for i in range(num_layers):
        lstm=tf.nn.rnn_cell.BasicLSTMCell(lstm_size)
        drop=tf.nn.rnn_cell.DropoutWrapper(lstm,output_keep_prob=keep_prob)
        cell_list.append(drop)
    cell=tf.nn.rnn_cell.MultiRNNCell(cell_list)
    initial_state=cell.zero_state(batch_size, tf.float32)
    return cell, initial_state
def build_output(lstm_output, in_size, out_size):
    x=tf.reshape(lstm_output, [-1, in_size])
    weights = tf.Variable(tf.truncated_normal([in_size, out_size + 1]))
    bias = tf.Variable(tf.zeros(shape=[out_size + 1]))
    logits = tf.nn.bias_add(tf.matmul(x, weights), bias=bias)
    out=tf.nn.softmax(logits, name='predictions')
    return out, logits
def build_loss(logits, targets, lstm_size, num_classes):
    label=tf.one_hot(tf.reshape(targets,[-1]),depth=num_classes+1)
    loss=tf.nn.softmax_cross_entropy_with_logits(logits=logits, labels=label)
    loss=tf.reduce_mean(loss)
    return loss
def build_optimizer(loss, learning_rate, grad_clip):
    tvars=tf.trainable_variables()
    grads,_=tf.clip_by_global_norm(tf.gradients(loss, tvars), grad_clip)
    train_op=tf.train.AdamOptimizer(learning_rate)
    optimizer=train_op.apply_gradients(zip(grads, tvars))
    return optimizer
def train():
    inputs = tf.placeholder(dtype=tf.int32, shape=(None, None), name='inputs')
    targets = tf.placeholder(dtype=tf.int32, shape=(None, None), name='targets')
    keep_prob = tf.placeholder(tf.float32, name='keep_prob')
    batch_size = 1
    lstm_size = 64
    num_layers = 2
    learning_rate = 0.001
    epochs = 1100
    content_vec, char_to_int, all_char, int_to_char = data_measure('TrainData.txt')
    x_batches, y_batches = get_batches(batch_size=batch_size, content_vec=content_vec, char_to_int=char_to_int)
    num_classes=len(all_char)
    cell, initial_state = build_lstm(lstm_size, num_layers, batch_size, keep_prob=keep_prob)
    embedding = tf.get_variable('embedding', initializer=tf.random_uniform(
        [num_classes + 1, lstm_size], -1.0, 1.0))
    x_one_hot = tf.nn.embedding_lookup(embedding, inputs)#tf.nn.embedding_lookup函数的用法主要是选取一个张量里面索引对应的元素。tf.nn.embedding_lookup（params, ids）:params可以是张量也可以是数组等，id就是对应的索引，其他的参数不介绍。
    outputs, state = tf.nn.dynamic_rnn(cell, x_one_hot, initial_state=initial_state)
    prediction, logits = build_output(outputs, lstm_size, num_classes)
    loss = build_loss(logits, targets, lstm_size, num_classes)
    optimizer = build_optimizer(loss, learning_rate, 5)
    test_prediction=tf.argmax(prediction,1,name='prediction')
    saver = tf.train.Saver(max_to_keep=1)
    with tf.Session() as sess:
        #sess.run(tf.global_variables_initializer())#重新初始化模型
        saver.restore(sess, './rnn_model/my_model.ckpt')#读取先前训练的模型参数
        counter = 0
        for e in range(epochs):
            print('{} epoch is done'.format(e))
            new_state=sess.run(initial_state)
            data_idx=0
            for n in range(len(x_batches)):
                x=x_batches[n]
                y=y_batches[n]
                counter += 1
                feed={inputs: x,targets: y,keep_prob: 0.5,initial_state: new_state}
                batch_loss, new_state,_=sess.run([loss,state,optimizer],feed_dict=feed)
                '''
                print('轮数: {}/{}。。。'.format(e + 1, epochs),
                      '正在训练的样本编号：{}'.format(data_idx),
                      '训练步数：{}。。。'.format(counter),
                      '训练误差：{}。。。'.format(batch_loss), )
                data_idx = (data_idx + 1) % num
                '''
        saver.save(sess, "./rnn_model/my_model.ckpt")
        print('模型更新了')

def createNewAbcFile():
    M = ["4/4", "6/8", "9/8", "6/4", "5/4"]
    L = ["1/8", "1/4", "-1"]
    K = ["D", "Bb", "A", "Am", "Dm", "G", "Am", "C", "Gm", "Em", "E", "F"]
    idx = np.random.randint(0, len(M), 1)
    curM = M[idx[0]]
    idx = np.random.randint(0, len(L), 1)
    curL = L[idx[0]]
    idx = np.random.randint(0, len(K), 1)
    curK = K[idx[0]]
    param = {}
    emo = np.random.randint(1, 3, 1)
    with open("parameter.json", 'r') as f:
        param = json.load(f)
        f.close()
    name = param['N']
    maxLen = int(param['ML'])
    newAbc = ""
    newAbc = newAbc + ("O:" + param['O'] if (param['O'] != "-1") else "O:" + str(emo[0])) + "\n"
    newAbc = newAbc + ("M:" + param['M'] + "\n" if (param['M'] != "-1") else "M:" + curM + "\n")
    newAbc = newAbc + ("L:" + param['L'] + "\n" if (param['L'] != "-1") else ("" if (curL == "-1") else "L:" + curL + "\n"))
    newAbc = newAbc + ("K:" + param['K'] + "\n" if (param['K'] != "-1") else "K:" + curK + "\n")
    abcFile = open(name + ".txt", mode="w")
    abcFile.write(newAbc)
    abcFile.close()
    os.remove("parameter.json")
    return name,maxLen
def writeToAbcFile(name,result):
    abcFile=open(name+'.txt','w')
    abcFile.write("X:1")
    abcFile.write(result[3:])
    abcFile.close()
def get_test(file_name, char_to_int, all_char, int_to_char):
    with open(file_name) as f:
        text=f.read()
    content = text.split('@')
    content = content[:1]
    content_vec = [list(map(lambda word: char_to_int.get(word, len(all_char)), a_music)) for a_music in content]
    content_vec=np.array(content_vec)
    return content_vec
def test():
    cv, char_to_int, all_char, int_to_char = data_measure('TrainData.txt')
    sess = tf.Session()
    inputs = tf.placeholder(dtype=tf.int32, shape=(None, None), name='inputs')
    targets = tf.placeholder(dtype=tf.int32, shape=(None, None), name='targets')
    keep_prob = tf.placeholder(tf.float32, name='keep_prob')
#################################################
    batch_size = 1
    lstm_size = 64
    num_layers = 2
    learning_rate = 0.001
    num_classes = len(all_char)
    cell, initial_state = build_lstm(lstm_size, num_layers, batch_size, keep_prob=keep_prob)
    embedding = tf.get_variable('embedding', initializer=tf.random_uniform(
        [num_classes + 1, lstm_size], -1.0, 1.0))
    x_one_hot = tf.nn.embedding_lookup(embedding, inputs)
    outputs, state = tf.nn.dynamic_rnn(cell, x_one_hot, initial_state=initial_state)
    prediction, logits = build_output(outputs, lstm_size, num_classes)
    test_prediction = tf.argmax(prediction, 1, name='prediction')
    loss = build_loss(logits, targets, lstm_size, num_classes)
    optimizer = build_optimizer(loss, learning_rate, 5)
#########################################################
    saver = tf.train.Saver(tf.global_variables())
    saver.restore(sess, './rnn_model/my_model.ckpt')  # 读取模型里的各种参数
    fileName,maxLen=createNewAbcFile()
    inp=get_test(fileName+'.txt', char_to_int, all_char, int_to_char)
    max_len = maxLen                                       #指定生成的音乐长度
    inp_len = len(inp[0])
######################################
    x_init = np.array(char_to_int['<']).reshape(1, 1)
    feed_dict_init = {inputs: x_init, keep_prob: 0.5}
    _, newstate = sess.run([prediction, state], feed_dict=feed_dict_init)
################################
    result = inp.copy()
    result = result.tolist()
    if max_len==-1:
        i=0
        while 1:
            if i < inp_len:
                w = inp[0][i]
                x = np.array(w).reshape(1, 1)
                feed_dict = {inputs: x, keep_prob: 0.5, initial_state: newstate}
                predict, newstate = sess.run([test_prediction, state], feed_dict=feed_dict)
                if i == inp_len - 1:
                    result[0].append(predict[0])
            else:
                feed_dict = {inputs: x, keep_prob: 0.5, initial_state: newstate}
                predict, newstate = sess.run([test_prediction, state], feed_dict=feed_dict)
                w = predict
                w = w.tolist()
                x = np.array(w).reshape(1, 1)
                if int_to_char[w[0]] == '>':
                    break
                result[0].append(w[0])
            i=i+1
    else:
        for i in range(max_len):
            if i < inp_len:
                w = inp[0][i]
                x = np.array(w).reshape(1, 1)
                feed_dict = {inputs: x, keep_prob: 0.5, initial_state: newstate}
                predict, newstate = sess.run([test_prediction, state], feed_dict=feed_dict)
                if i==inp_len-1:
                    result[0].append(predict[0])
            else:
                feed_dict = {inputs: x, keep_prob: 0.5, initial_state: newstate}
                predict, newstate = sess.run([test_prediction, state], feed_dict=feed_dict)
                w = predict
                w = w.tolist()
                x = np.array(w).reshape(1, 1)
                if int_to_char[w[0]] == '>':
                    break
                result[0].append(w[0])
    #print(result)
    #print(len(result[0]))
    prdic_music = ""
    for i in result[0]:
        prdic_music += int_to_char[i]
    #print("")
    #print(prdic_music)
    return fileName,prdic_music
def isNumber(c):
    if(c>='0' and c<='9'):
        return 1
    return 0
def detectError(result):
    newRes=''
    isChange=0
    isVertical=0
    for i in range(0,len(result)):
        if(result[i]=='[' or result[i]==']'):
            isChange=1
            continue
        if(isVertical==1 and (result[i]==':' or isNumber(result[i])==1)):
            isChange=1
            continue
        if(result[i]=='|'):
            isVertical=1
        else:
            isVertical=0
        newRes=newRes+result[i]
    return newRes
def generation(fileName,result):
    writeToAbcFile(fileName, result)
    command="abc2midi.exe "+fileName+".txt -o result\\"+fileName+".mid"
    os.system(command)
    os.remove(fileName+".txt")
#train()
fileName,reuslt=test()
#reuslt=detectError(reuslt)
print(reuslt)
generation(fileName,reuslt)