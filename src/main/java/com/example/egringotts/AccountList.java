package com.example.egringotts;

import java.util.ArrayList;
import java.util.List;

public class AccountList<T> {
    private List<T> accounts;

    public AccountList() {
        this.accounts = new ArrayList<>();
    }

    public void addAccount(T account) {
        this.accounts.add(account);
    }

    public void removeAccount(T account) {
        this.accounts.remove(account);
    }

    public T getAccount(int index) {
        return this.accounts.get(index);
    }

    public int size() {
        return this.accounts.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account List:\n");
        for (int i = 0; i < this.accounts.size(); i++) {
            T account = this.accounts.get(i);
            sb.append(i + 1).append(". ");
            sb.append(account.toString()).append("\n");
        }
        return sb.toString();
    }
}
