import requests
from app import app, db
from flask import Flask, url_for, redirect, render_template, request
from app.model.tables import Mensagem, Paciente, Consulta, Pagamento
import json



@app.route("/home/<int:id>", methods = ['GET', 'POST'])
def home(id):
    pacienteLog = Paciente.query.get(id)
    return render_template('homeuser.html', paciente = pacienteLog)



@app.route("/cadastro", methods = ['GET', 'POST'])
def cadastro():
    if request.method=='POST':

        url = f"https://viacep.com.br/ws/{request.form['CEP']}/json/"
        logradouro = requests.get(url).json()['logradouro']
        complemento = request.form['complemento']
        bairro = requests.get(url).json()['bairro']
        cidade = requests.get(url).json()['localidade']
        uf = requests.get(url).json()['uf']
        endereco = logradouro + ", " + complemento + ", " + bairro + ", " + request.form['CEP'] + ", " + cidade + ", " + uf

        if (request.form['password']) == (request.form['passwordConfirm']):
                    novoPaciente = Paciente(request.form['CPF'], 
                                request.form['nome'], 
                                request.form['sexo'], 
                                request.form['nascimento'],
                                endereco,
                                request.form['email'],
                                request.form['telefone'],
                                ' ',
                                request.form['password'])

                    db.session.add(novoPaciente)
                    db.session.commit()
                    return redirect("http://127.0.0.1:5001/")
        else:
            return render_template("cadastro.html", erro = 'Ops, as senhas não são iguais!')

    return render_template("cadastro.html")



@app.route("/editarcadastro/<int:id>", methods = ['GET', 'POST'])
def editarcadastro(id):

    paciente = Paciente.query.get(id)

    dados = paciente.endereco.split(', ')
    r = dados[0]
    c = dados[1]
    cep = dados[3]


    if request.method == 'POST':

        url = f"https://viacep.com.br/ws/{request.form['CEP']}/json/"
        logradouro = requests.get(url).json()['logradouro']
        complemento = request.form['complemento']
        bairro = requests.get(url).json()['bairro']
        cidade = requests.get(url).json()['localidade']
        uf = requests.get(url).json()['uf']
        endereco = logradouro + ", " + complemento + ", " + bairro + ", " + request.form['CEP'] + ", " + cidade + ", " + uf

        paciente.nome = request.form['nome']
        paciente.sexo = request.form['sexo']
        paciente.endereco = endereco
        paciente.email = request.form['email']
        paciente.telefone = request.form['telefone']
        paciente.senha = request.form['password']

        db.session.commit()
        return render_template("editcadastro.html", paciente = paciente, r = r, c = c, cep = cep, mensagem = "Alterações salvas com sucesso!")

    return render_template("editcadastro.html", paciente = paciente, r = r, c = c, cep = cep)



@app.route("/delete/<int:id>")
def delete(id):

    paciente = Paciente.query.get(id)

    db.session.delete(paciente)
    db.session.commit()

    return redirect(url_for("index"))



@app.route("/mensagens/<int:id>", methods = ['GET', 'POST'])
def mensagem(id):
    pacienteLog = Paciente.query.get(id)
    mensagens = Mensagem.query.all()

    if request.method == 'POST':
        novaMensagem = Mensagem(request.form['assunto'], request.form['conteudo'], id)
        db.session.add(novaMensagem)
        db.session.commit()
        url = f"http://127.0.0.1.5000/mensagens/{id}"

        return redirect(url_for('mensagem', id = id))

    return render_template("mensagens.html", paciente = pacienteLog, mensagens = mensagens)