import requests
from app import app, db
from flask import Flask, url_for, redirect, render_template, request
import json

users = [{
    "nome": "Carolina Ramos Bertonlino",
    "cpf": "0001",
    "senha": "admin"
},
{
    "nome": "Fábio",
    "cpf": "2",
    "senha": "admin"
}]


@app.route("/", methods = ['GET', 'POST'])
def index():

    if request.method=='POST':
        if str(request.form['user']) == '1' and str(request.form['pass']) == 'senha':
            return redirect(url_for())

        for paciente in users:
            if str(request.form['user']) == str(paciente['cpf']) and str(request.form['pass']) == str(paciente['senha']):
                cpf = int(paciente['cpf'])
                url = f"http://127.0.0.1:5000/home/{cpf}"
                return redirect(url)

        else:
            return render_template("agendamento.html", erro = "Ops, os dados inseridos são inválidos!")

    return render_template("agendamento.html")
