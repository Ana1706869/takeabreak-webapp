#!/usr/bin/env python3
import sqlite3

db_path = r'd:/Projeto de Informática/Take a Break App/data/AgendamentoFolgas.db'
con = sqlite3.connect(db_path)
cur = con.cursor()

print("=== Folgas distribution by Funcionario ===")
result = cur.execute("""
SELECT f.FuncionarioId, func.Nome, COUNT(*) as count 
FROM Folgas f
LEFT JOIN [Funcionário] func ON f.FuncionarioId = func.FuncionarioId
GROUP BY f.FuncionarioId
ORDER BY count DESC
""").fetchall()

for func_id, nome, count in result:
    print(f"ID: {func_id}, Nome: {nome}, Folgas: {count}")

print("\n=== All Funcionários ===")
all_funcs = cur.execute("SELECT FuncionarioId, Nome FROM [Funcionário] ORDER BY FuncionarioId").fetchall()
for func_id, nome in all_funcs:
    print(f"ID: {func_id}, Nome: {nome}")

con.close()
